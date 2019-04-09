package com.kpiroom.bubble.util.progressState


data class ProgressState(
    val state: Int,
    val message: String? = null,
    val callback: ((Boolean) -> Unit)? = null,
    val firstOption: String? = null,
    val secondOption: String? = null
) {
    companion object {
        const val LOADING = 0
        const val ALERT = 1
        const val FINISHED = 2
    }
}