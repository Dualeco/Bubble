package com.kpiroom.bubble.util.collectionsAsync

import android.util.Log
import com.kpiroom.bubble.util.async.AsyncBag
import com.kpiroom.bubble.util.async.AsyncProcessor
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.resume

suspend fun <T, R> List<T>.mapAsync(
    bag: AsyncBag,
    load: suspend (T) -> R?
): List<R> =
    suspendCancellableCoroutine { continuation ->
        val count = AtomicInteger(0)

        Collections.synchronizedList(mutableListOf<R>()).let { result ->
            forEach { item ->
                AsyncProcessor {
                    load(item)?.let { result.add(it) }
                    if (count.incrementAndGet() == size)
                        continuation.resume(result)

                } runWith (bag)
            }
        }
    }