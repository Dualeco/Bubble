package com.kpiroom.bubble.util.progressState


data class ProgressState(
    val state: Int,
    val message: String? = null,
    val alertCallback: ((Boolean) -> Unit)? = null,
    val firstOption: String? = null,
    val secondOption: String? = null,
    val inputCallback: ((String) -> Unit)? = null
) {
    companion object {
        const val LOADING = 0
        const val ALERT = 1
        const val FINISHED = 2
        const val INPUT = 3
    }
}