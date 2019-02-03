package com.kpiroom.bubble.util.databinding


data class ProgressStateContainer(
    val state: ProgressState,
    val message: String? = null,
    val callback: ((Boolean) -> Unit)? = null
)