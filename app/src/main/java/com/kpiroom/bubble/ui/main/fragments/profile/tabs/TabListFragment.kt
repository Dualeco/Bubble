package com.kpiroom.bubble.ui.main.fragments.profile.tabs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dichotome.profilebar.stubs.TabListAdapter
import com.dichotome.profilebar.stubs.TabListItem
import com.dichotome.profilebar.ui.tabPager.TabFragment
import com.kpiroom.bubble.R

abstract class TabListFragment(itemViewType: Int, isThumbnailCircular: Boolean) : TabFragment() {
    var items: List<TabListItem>
        get() = adapter.data
        set(value) {
            adapter.updateData(value)
        }

    var recyclerView: RecyclerView? = null
        set(value) {
            field = value?.also {
                it.adapter = adapter
            }
        }
    val adapter: TabListAdapter = TabListAdapter(listOf(), itemViewType, isThumbnailCircular)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.profile_tab_favorites, container, false)

}