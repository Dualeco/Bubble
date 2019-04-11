package com.kpiroom.bubble.util.view

import android.animation.ValueAnimator
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.core.view.isVisible


fun ViewPropertyAnimator.addTo(collection: MutableCollection<ViewPropertyAnimator>) = apply { collection.add(this) }

fun ValueAnimator.addTo(collection: MutableCollection<ValueAnimator>) = also { collection.add(it) }
