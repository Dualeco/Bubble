package com.kpiroom.bubble.util.events


class DelayedAction(val delay: Long, private val action: () -> Unit) {

    fun start(): Unit = action()
}