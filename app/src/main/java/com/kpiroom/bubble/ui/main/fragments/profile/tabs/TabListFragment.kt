package com.kpiroom.bubble.ui.main.fragments.profile.tabs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dichotome.profilebar.ui.tabPager.TabFragment
import com.kpiroom.bubble.R

abstract class TabListFragment(itemViewType: Int, isThumbnailCircular: Boolean) : TabFragment() {

    var recyclerView: RecyclerView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.profile_tab_favorites, container, false)

}