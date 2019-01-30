package com.kpiroom.bubble.util.async

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import java.util.concurrent.atomic.AtomicBoolean

class AsyncBag {

    private val jobs = ArrayList<Job>()
    private val isCanceled: AtomicBoolean = AtomicBoolean(false)

    fun add(job: Job): Boolean = synchronized(this) {
        trim()
        if (!isCanceled.get()) jobs.add(job) else job.cancel()
        !isCanceled.get()
    }

    fun cancel() {
        if (isCanceled.getAndSet(true)) return
        trim()
        synchronized(this) {
            for (job in jobs) {
                try {
                    job.cancelChildren()
                } catch (e: CancellationException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun trim() {
        synchronized(this) { jobs.removeAll { it.isCancelled || it.isCompleted } }
    }
}