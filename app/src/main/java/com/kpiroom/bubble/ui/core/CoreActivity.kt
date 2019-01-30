package com.kpiroom.bubble.ui.core

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class CoreActivity<V : CoreLogic, B : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var logic: V
    protected lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logic = provideLogic()
        binding = provideLayout().createBinding()
    }

    abstract fun provideLogic(): V

    abstract fun provideLayout(): LayoutBuilder

    inner class LayoutBuilder(private val layout: Int, private val editing: B.() -> Unit = {}) {

        fun createBinding() = DataBindingUtil.setContentView<B>(this@CoreActivity, layout).apply {
            setLifecycleOwner(this@CoreActivity)
            editing()
            executePendingBindings()
        }
    }
}