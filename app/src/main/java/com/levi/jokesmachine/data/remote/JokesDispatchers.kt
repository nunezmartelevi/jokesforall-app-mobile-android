package com.levi.jokesmachine.data.remote

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val jokesDispatchers: JokesDispatchers)

enum class JokesDispatchers {
    Default,
    IO
}
