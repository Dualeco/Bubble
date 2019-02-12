package com.kpiroom.bubble.util.event


class DelayedAction(val doAction: () -> Unit, val delay: Long)