# Flow + Compose patterns

The rule:

- **Repositories return cold `Flow<T>`.** Cold Flows compose — `combine`, `flatMapLatest`, cache-then-network, merging across repos.
- **ViewModels expose hot `StateFlow<UiState<T>>`** built with `.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Pending())`. No bare `viewModelScope.launch { flow.collect { ... } }`.
- **Screens collect with `collectAsStateWithLifecycle()`.** Lifecycle-aware; stops when backgrounded.

## The three layers

### Repository — cold Flow, non-null

```kotlin
interface UserRepository {
    fun getUsers(): Flow<List<User>>
}

class UserRepositoryImpl(private val service: UserService) : UserRepository {
    override fun getUsers(): Flow<List<User>> = flow {
        emit(service.getUsers().results)
    }
}
```

Don't wrap with `flowOn(Dispatchers.IO)` if your network client already dispatches off the main thread (Retrofit's `suspend` functions do; OkHttp handles the thread pool).

### ViewModel — `asUiState()`, trigger flow for imperative refresh

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class UserViewModel @Inject constructor(
    repository: UserRepository,
) : ViewModel() {
    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1).also { it.tryEmit(Unit) }

    val uiState: StateFlow<UiState<List<User>>> = refreshTrigger
        .flatMapLatest { repository.getUsers().asUiState() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Pending())

    fun refreshUsers() { refreshTrigger.tryEmit(Unit) }
}
```

- `asUiState()` wraps the repo Flow: emits `Pending` on start, `Done(data)` on success, `Failed(message)` on error.
- `MutableSharedFlow<Unit>(replay = 1)` seeds the first emission; tapping refresh re-emits.
- `flatMapLatest` cancels any in-flight request when a new trigger arrives.
- `WhileSubscribed(5_000)` keeps the flow alive across brief recompositions / config changes, tears down if nothing subscribes for 5 s.

### Screen — `collectAsStateWithLifecycle`, `when` on state

```kotlin
val state by vm.uiState.collectAsStateWithLifecycle()

when (val s = state) {
    is UiState.Pending -> Text("Loading…")
    is UiState.Done -> LazyColumn {
        items(s.data, key = { it.id.value }) { user -> UserRow(user, onNavigate) }
    }
    is UiState.Failed -> Text(s.message)
}
```

## `UiState<T>` — the shared async state type

All async UI state flows through a single `UiState<T>`:

```kotlin
sealed interface UiState<out T> {
    data class Pending<T>(val previous: T? = null) : UiState<T>
    data class Done<T>(val data: T) : UiState<T>
    data class Failed(val message: String) : UiState<Nothing>
}
```

**Why Pending / Done / Failed** — symmetric vocabulary, all expressing "has this settled?". Loading/Success/Error mixes grammatical forms and `Error` collides with `kotlin.Error`. `Done` and `Failed` are unambiguous terminal states; `Pending` is unambiguous non-terminal.

**`Pending(previous: T?)`** handles the refresh case. `Pending(previous = null)` is initial load (spinner, no data). `Pending(previous = lastList)` is pull-to-refresh (show old list + spinner). The UI checks `s.previous != null` to decide.

**`asUiState()` extension** lives alongside the type:

```kotlin
fun <T> Flow<T>.asUiState(): Flow<UiState<T>> = this
    .map<T, UiState<T>> { UiState.Done(it) }
    .onStart { emit(UiState.Pending()) }
    .catch { emit(UiState.Failed(it.message ?: "Something went wrong")) }
```

### When to define a per-screen sealed class instead

`UiState<T>` covers the 80% case. Define your own sealed class when:

- You need `Empty` as distinct from `Done(emptyList())` — different UX copy, different illustration.
- You need `Refreshing` with visible progress over old data AND you need the type to encode it (vs. `Pending(previous)`).
- You need typed errors: `NetworkError` / `AuthError` / `ParseError` — not just a string message.
- You need an `Idle` state for screens that don't load until the user acts (search screens).

In these cases, define a per-screen sealed interface next to the ViewModel and don't involve `UiState<T>`.

### Don't split `when` branches into separate Composables

```kotlin
// Don't do this unless a branch is genuinely complex (>~20 lines)
when (val s = state) {
    is UiState.Pending -> UsersLoadingScreen()
    is UiState.Done -> UsersSuccessScreen(s.data, onNavigate)
    is UiState.Failed -> UsersErrorScreen(s.message)
}
```

Extract a branch only when it grows or when the Composable is reused in ≥ 2 places. A 5-line `Text(...)` or `Column { Text; Text }` doesn't clear the bar.

## Combining sources

Combining is the payoff for the cold-Flow choice at the repo layer:

```kotlin
fun users(): Flow<List<User>> = combine(
    userDao.observeAll(),
    networkTrigger.flatMapLatest { flowOf(api.fetchUsers()) },
) { cached, fresh -> fresh.ifEmpty { cached } }
```

If this had been `suspend fun users(): List<User>`, you couldn't express it without awkward glue in the ViewModel.

## Bridging callback-based APIs

When a third-party SDK uses listeners/callbacks (payment SDKs, Bluetooth, geolocation, legacy Java APIs), adapt at the repository layer — don't reach for `viewModelScope.launch { sdk.doThing { result -> ... } }`. That leaks the callback's control flow into the ViewModel and loses cancellation.

**Single result → `suspendCancellableCoroutine`:**

```kotlin
suspend fun startTransaction(params: TransactionParams): TransactionResult =
    suspendCancellableCoroutine { cont ->
        val listener = object : TransactionListener {
            override fun onResult(result: TransactionResult) = cont.resume(result)
            override fun onError(e: Throwable) = cont.resumeWithException(e)
        }
        sdk.startTransaction(params, listener)
        cont.invokeOnCancellation { sdk.cancelTransaction() }
    }
```

**Stream of events → `callbackFlow`:**

```kotlin
fun transactionEvents(): Flow<TransactionEvent> = callbackFlow {
    val listener = TransactionListener { event -> trySend(event) }
    sdk.addListener(listener)
    awaitClose { sdk.removeListener(listener) }
}
```

The ViewModel then consumes these the same way as any other Flow — `flatMapLatest`, `stateIn`, `asUiState()`. The callback boundary stops at the repo.

## When to break the rule

One-shot fire-and-forget events (analytics hit, post a command, delete a row) where nothing observes a result — use `viewModelScope.launch { repo.suspendCall() }`. Don't fake a Flow for consistency.

## Anti-patterns

- `viewModelScope.launch { sdk.doAsync { result -> _state.value = ... } }` — callback-inside-launch. Bridge with `suspendCancellableCoroutine` or `callbackFlow` at the repo instead.
- `.collect { _state.value = it }` inside a ViewModel — use `.stateIn` instead.
- `.flowOn(Dispatchers.IO)` wrapping Retrofit suspend calls — redundant; OkHttp's dispatcher already handles it.
- `Flow<T?>` on the repository interface — null-as-sentinel in the data layer. Use `asUiState()` and let `Pending` carry the initial state.
- `.catch { Log.e(...) }` without `emit(...)` — the flow terminates silently and the UI stays on its initial `stateIn` value forever. `asUiState()` handles this correctly.
- `collectAsState()` in a Composable — use `collectAsStateWithLifecycle()`.
- `remember(x) { x ?: default }` — recomputes on every change; just write `x ?: default`.
