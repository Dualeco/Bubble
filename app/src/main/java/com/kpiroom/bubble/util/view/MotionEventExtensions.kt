package com.kpiroom.bubble.util.view

import android.view.MotionEvent
import android.view.View


fun MotionEvent.isWithinView(view: View): Boolean =
    (action == MotionEvent.ACTION_UP).let {
        val srcCoords = IntArray(2)
        view.getLocationOnScreen(srcCoords)

        val x = rawX + view.left - srcCoords[0]
        val y = rawY + view.top - srcCoords[1]

        if (x < view.left || x > view.right || y < view.top || y > view.bottom) {
            return false
        }
        return true
    }
