package com.kpiroom.bubble.ui.main.fragments.profile.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dichotome.profilebar.ui.tabPager.TabFragment
import com.kpiroom.bubble.util.recyclerview.TabCoreAdapter
import com.kpiroom.bubble.util.recyclerview.TabCoreHolder
import com.kpiroom.bubble.util.recyclerview.items.TabCoreItem

abstract class CoreTabFragment<I : TabCoreItem, VH : TabCoreHolder<I>>(
    private val layoutId: Int
) : TabFragment() {

    abstract val adapter: TabCoreAdapter<I, VH>

    var items: MutableList<I>
        get() = adapter.data
        set(value) = adapter.updateTo(value)

    var recyclerView: RecyclerView? = null
        set(value) {
            field = value?.also {
                it.adapter = adapter
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(layoutId, container, false)
}