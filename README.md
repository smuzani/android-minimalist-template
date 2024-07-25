# Android Minimalist Template
A minimalist template with networking, Compose, and Flow.

![Architecture Image](architecture.png)

Thanks to [https://randomuser.me/](https://randomuser.me) for the free sample API.

Reminder: Rename `Template` and the package name to your app name.

## Stack

**Network:** Retrofit (API), Coil (images)

**UI:** Jetpack Compose (where stuff is), Material 3 (design/color)

**Logic:** Kotlin Flow (Reactive programming), Hilt (DI)

**Tests**: JUnit (unit). UI tests can be done on [Maestro](https://maestro.mobile.dev/). One example is given in [Intro.yaml](https://github.com/smuzani/android-minimalist-template/blob/main/app/src/maestro/Intro.yaml). 

****

## Philosophy

### Peripheral Vision

Quoting Paul Graham in [Great Hackers](https://paulgraham.com/gh.html),

> John McPhee wrote that Bill Bradley's success as a basketball player was due partly to his extraordinary peripheral vision. "Perfect" eyesight means about 47 degrees of vertical peripheral vision. Bill Bradley had 70; he could see the basket when he was looking at the floor. Maybe great hackers have some similar inborn ability. (I cheat by using a very dense language, which shrinks the court.)

The cheat here is to keep the code denser. There should be no tunnel of 'clean code', no chain of factories, but rather putting related code closer to each other.

### AI Ready

Related to the peripheral vision, you should be able to paste a file and related interfaces into GPT and ask it to add features. A file should not have too many implementation details. If AI can't navigate it easily, humans will have trouble too.

### Minimalist

Everything in the stack should improve peripheral vision. I debated for a while whether to add DI even though it's omnipresent in Android architectures. Material3 went in and out of the design. Ask yourself whether anything new improves or takes away from it.
