package com.kpiroom.bubble.os

import android.view.ViewPropertyAnimator


fun ViewPropertyAnimator.addTo(collection: MutableCollection<ViewPropertyAnimator>) = apply { collection.add(this) }
