package com.kpiroom.bubble.util.view

import android.animation.ValueAnimator
import android.view.ViewPropertyAnimator


fun ViewPropertyAnimator.addTo(collection: MutableCollection<ViewPropertyAnimator>) = apply { collection.add(this) }

fun ValueAnimator.addTo(collection: MutableCollection<ValueAnimator>): ValueAnimator {
    collection.add(this)
    return this
}