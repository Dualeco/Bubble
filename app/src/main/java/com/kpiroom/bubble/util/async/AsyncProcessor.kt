package com.kpiroom.bubble.util.async

import com.kpiroom.bubble.util.exceptions.core.CoreException
import com.kpiroom.bubble.util.exceptions.ErrorHelper
import kotlinx.coroutines.*

class AsyncProcessor(val task: suspend CoroutineScope.() -> Unit) {
    private val context = Dispatchers.Default
    private var errorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
        GlobalScope.launch(Dispatchers.Main) { throw e }
    }
    private var cancelComposite: AsyncBag = AsyncBag()

    infix fun handleError(errHandler: (CoreException) -> Unit): AsyncProcessor {
        errorHandler = CoroutineExceptionHandler { _, e ->
            GlobalScope.launch(Dispatchers.Main) {
                errHandler(ErrorHelper.resolve(e))
            }
        }
        return this
    }

    private fun cancelWith(composite: AsyncBag): AsyncProcessor {
        cancelComposite = composite
        return this
    }

    private fun run(): Job = GlobalScope.launch(context + errorHandler) {
        task()
    }.cancelWith(cancelComposite)

    infix fun runWith(composite: AsyncBag): Job = cancelWith(composite).run()
}

fun Job.cancelWith(composite: AsyncBag): Job {
    composite.add(this)
    return this
}