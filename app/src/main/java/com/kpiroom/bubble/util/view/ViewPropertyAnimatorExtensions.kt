package com.kpiroom.bubble.util.view

import android.view.ViewPropertyAnimator


fun ViewPropertyAnimator.addTo(collection: MutableCollection<ViewPropertyAnimator>) = apply { collection.add(this) }
