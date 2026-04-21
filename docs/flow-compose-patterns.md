# Flow + Compose patterns

The rule for this template:

- **Repositories return cold `Flow<T>`.** Cold Flows compose — `combine`, `flatMapLatest`, cache-then-network, merging across repos.
- **ViewModels expose hot `StateFlow<T>`** built with `.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), initial)`. No bare `viewModelScope.launch { flow.collect { ... } }`.
- **Screens collect with `collectAsStateWithLifecycle()`.** Lifecycle-aware; stops when backgrounded.

## The three layers

### Repository — cold Flow, non-null

```kotlin
interface RandomUserRepository {
    fun get50RandomUsers(): Flow<List<RandomUser>>
}

class RandomUserRepositoryImpl(private val service: RandomUserService) : RandomUserRepository {
    override fun get50RandomUsers(): Flow<List<RandomUser>> = flow {
        emit(service.getRandomUsers().results)
    }
}
```

No `flowOn(Dispatchers.IO)` — Retrofit's `suspend` functions already dispatch on OkHttp's pool.

### ViewModel — `stateIn`, trigger flow for imperative refresh

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class UserViewModel @Inject constructor(
    repository: RandomUserRepository,
) : ViewModel() {
    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1).also { it.tryEmit(Unit) }

    val users: StateFlow<List<RandomUser>?> = refreshTrigger
        .flatMapLatest { repository.get50RandomUsers() }
        .catch { e -> Log.e("UserViewModel", "Error", e) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun refreshUsers() { refreshTrigger.tryEmit(Unit) }
}
```

- `MutableSharedFlow<Unit>(replay = 1)` seeds the first emission in `init`; tapping the refresh button re-emits.
- `flatMapLatest` cancels any in-flight request when a new trigger arrives.
- `.catch` handles errors inside the pipeline (no `runCatching`, no `try/catch`).
- `WhileSubscribed(5_000)` keeps the flow alive across brief recompositions / config changes, tears down if nothing subscribes for 5 s.
- The initial `null` is the "not loaded yet" signal — see *Why no sealed `UiState`* below.

### Screen — `collectAsStateWithLifecycle`

```kotlin
val users by vm.users.collectAsStateWithLifecycle()

if (users == null) {
    Text("Loading…", style = MaterialTheme.typography.displayLarge)
} else {
    LazyColumn {
        items(users.orEmpty(), key = { it.id.value }) { user ->
            UserRow(user, onNavigateToDetails)
        }
    }
}
```

No initial-value argument — `StateFlow` already carries one.

## Combining sources

Combining is the payoff for the cold-Flow choice. Example — merging a cached DB stream with a network refresh:

```kotlin
fun users(): Flow<List<User>> = combine(
    userDao.observeAll(),
    networkTrigger.flatMapLatest { flowOf(api.fetchUsers()) },
) { cached, fresh -> fresh.ifEmpty { cached } }
```

If this had been `suspend fun users(): List<User>` at the repo, you couldn't express it without awkward glue in the ViewModel.

## When to break the rule

One-shot fire-and-forget events (log an analytics hit, post a command, delete a row) where nothing observes a result — use a plain `suspend fun` and `viewModelScope.launch { repo.call() }`. Don't fake a Flow just to stay consistent.

## Why no sealed `UiState`

The template uses `StateFlow<T?>` where `null` means "not loaded yet", rather than:

```kotlin
sealed class UiState<T> { object Loading; data class Success<T>(...); data class Error(...) }
```

Reach for the sealed version when you have:

- Real error UI (retry button, error banner, differentiated messages).
- Refresh-while-keeping-old-data (need `Loading` + prior `Success` simultaneously).
- ≥ 2 screens sharing the shape — the abstraction earns its keep once it's used twice.

Until then, `T?` is enough and keeps the `when`-ladders out of every screen.

## Anti-patterns

- `.collect { _state.value = it }` inside a ViewModel — use `.stateIn` instead.
- `.flowOn(Dispatchers.IO)` wrapping Retrofit suspend calls — redundant.
- `Flow<T?>` on the repository interface — null-as-sentinel in the data layer. Loading belongs in the VM's initial `stateIn` value.
- `collectAsState()` in a Composable — use `collectAsStateWithLifecycle()`.
- `remember(x) { x ?: default }` — recomputes on every change; just write `x ?: default`.
