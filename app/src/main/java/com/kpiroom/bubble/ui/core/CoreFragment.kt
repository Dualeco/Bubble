package com.kpiroom.bubble.ui.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class CoreFragment<V : CoreLogic, B : ViewDataBinding> : Fragment() {

    protected lateinit var logic: V
    protected lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logic = provideLogic()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = provideLayout(inflater, container).createBinding()
        return binding.root
    }

    abstract fun provideLogic(): V

    abstract fun provideLayout(inflater: LayoutInflater, container: ViewGroup?): LayoutBuilder

    inner class LayoutBuilder(
        private val inflater: LayoutInflater,
        private val container: ViewGroup?,
        private val layout: Int,
        private val editing: B.() -> Unit = {}
    ) {

        fun createBinding() = DataBindingUtil.inflate<B>(inflater, layout, container, false).apply {
            lifecycleOwner = this@CoreFragment
            editing()
            executePendingBindings()
        }
    }
}