package com.kpiroom.bubble.util.collections


import android.util.ArrayMap
import com.kpiroom.bubble.util.async.AsyncBag
import com.kpiroom.bubble.util.async.AsyncProcessor
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.resume

suspend fun <K, V, R> Map<K, V>.mapAsync(
    bag: AsyncBag,
    transform: suspend (Map.Entry<K, V>) -> R
): Map<K, R> =
    if (isEmpty())
        mapOf()
    else
        suspendCancellableCoroutine { continuation ->
            val count = AtomicInteger(0)

            Collections.synchronizedMap(mutableMapOf<K, R>()).let { result ->
                forEach { item ->
                    AsyncProcessor {
                        transform(item)?.let {
                            result[item.key] = it
                        }
                        if (count.incrementAndGet() == size)
                            continuation.resume(result)

                    } runWith (bag)
                }
            }
        }
suspend fun <T, R> List<T>.mapAsync(
    bag: AsyncBag,
    transform: suspend (T) -> R?
): List<R> =
    if (isEmpty())
        listOf()
    else
        suspendCancellableCoroutine { continuation ->
            val count = AtomicInteger(0)

            Collections.synchronizedList(mutableListOf<R>()).let { result ->
                forEach { item ->
                    AsyncProcessor {
                        transform(item)?.let {
                            result.add(it)
                        }
                        if (count.incrementAndGet() == size)
                            continuation.resume(result)

                    } runWith (bag)
                }
            }
        }