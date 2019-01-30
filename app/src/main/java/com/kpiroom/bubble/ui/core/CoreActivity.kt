package com.kpiroom.bubble.ui.core

import android.app.Activity
import android.os.Bundle

abstract class CoreActivity<V : CoreLogic> : Activity() {

    protected lateinit var logic: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logic = provideLogic()
    }

    abstract fun provideLogic(): V
}