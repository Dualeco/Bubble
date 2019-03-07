package com.kpiroom.bubble.util.databinding


data class ProgressState(
        val state: Int,
        val message: String? = null,
        val callback: ((Boolean) -> Unit)? = null
) {
    companion object {
        const val LOADING = 0
        const val ALERT = 1
        const val FINISHED = 2
    }
}