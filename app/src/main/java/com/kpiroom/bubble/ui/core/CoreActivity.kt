package com.kpiroom.bubble.ui.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class CoreActivity<L : CoreLogic, B : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var logic: L
    protected lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logic = provideLogic()
        binding = provideLayout().createBinding()
    }

    abstract fun provideLogic(): L

    abstract fun provideLayout(): LayoutBuilder

    inner class LayoutBuilder(private val layout: Int, private val editing: B.() -> Unit = {}) {

        fun createBinding() = DataBindingUtil.setContentView<B>(this@CoreActivity, layout).apply {
            lifecycleOwner = this@CoreActivity
            editing()
            executePendingBindings()
        }
    }
}