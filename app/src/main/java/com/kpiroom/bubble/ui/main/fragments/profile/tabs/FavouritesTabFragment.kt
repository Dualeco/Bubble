package com.kpiroom.bubble.ui.main.fragments.profile.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dichotome.profilebar.ui.tabPager.TabFragment
import com.kpiroom.bubble.R
import kotlinx.android.synthetic.main.profile_tab_favorites.*

class FavouritesTabFragment : TabListFragment(
    R.layout.tab_item_favorites,
    false
) {
    companion object {
        fun newInstance(tabTitle: String) = FavouritesTabFragment().apply {
            title = tabTitle
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = favorites_recycler.apply {
            layoutManager = GridLayoutManager(context, 3)
        }
    }
}