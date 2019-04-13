package com.kpiroom.bubble.util.collections

import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.TimeRecord
import com.kpiroom.bubble.util.async.AsyncBag

fun <T> Map<T, Long>.toTimeRecordList() = map { TimeRecord(it.key, it.value) }

suspend fun <T, R> List<TimeRecord<T>>.mapDataAsync(
    bag: AsyncBag,
    transform: suspend (data: T) -> R?
): List<TimeRecord<R>> = mapAsync(bag) { transform(it.data)?.let { data -> TimeRecord(data, it.time) } }

fun <T> List<TimeRecord<T>>.sortedByLatest() = sortedByDescending { it.time }

fun <T> List<TimeRecord<T>>.entries() = map { it.data }