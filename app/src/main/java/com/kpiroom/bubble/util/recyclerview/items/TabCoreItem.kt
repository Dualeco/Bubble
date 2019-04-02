package com.kpiroom.bubble.util.recyclerview.items

import java.util.*

abstract class TabCoreItem(
    val name: String,
    val picUrl: String,
    val isPicCircular: Boolean,
    val uuid: String = UUID.randomUUID().toString()
)