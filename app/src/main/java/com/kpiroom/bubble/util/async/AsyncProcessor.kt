package com.kpiroom.bubble.util.async

import kotlinx.coroutines.*

class AsyncProcessor(private val task: suspend CoroutineScope.() -> Unit) {
    private var context: CoroutineDispatcher = Dispatchers.Main
    private var errorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
        GlobalScope.launch (Dispatchers.Main) { throw e }
    }
    private var cancelComposite: AsyncBag = AsyncBag()

    fun handleError(errHandler: (Throwable) -> Unit): AsyncProcessor {
        this.errorHandler = CoroutineExceptionHandler { _, e -> GlobalScope.launch(Dispatchers.Main) { errHandler(e) } }
        return this
    }

    fun cancelWith(composite: AsyncBag): AsyncProcessor {
        this.cancelComposite = composite
        return this
    }

    fun run(): Job = GlobalScope.launch(context + errorHandler) {
        task()
    }.cancelWith(cancelComposite)

    fun run(composite: AsyncBag): Job = cancelWith(composite).run()
}

fun Job.cancelWith(composite: AsyncBag): Job {
    composite.add(this)
    return this
}