package com.kpiroom.bubble.ui.main.fragments.profile.tabs

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.kpiroom.bubble.R
import com.kpiroom.bubble.util.recyclerview.TabGridAdapter
import com.kpiroom.bubble.util.recyclerview.TabGridHolder
import com.kpiroom.bubble.util.recyclerview.items.TabGridItem
import kotlinx.android.synthetic.main.profile_tab_favorites.*

class FavouritesTabFragment(favoritesAdapter: TabGridAdapter) :
    CoreTabFragment<TabGridItem, TabGridHolder>(R.layout.profile_tab_favorites) {

    companion object {
        fun newInstance(
            adapter: TabGridAdapter,
            tabTitle: String
        ) = FavouritesTabFragment(adapter).apply {
            title = tabTitle
        }
    }

    override val adapter = favoritesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = favorites_recycler.apply {
            layoutManager = GridLayoutManager(context, 3)
        }
    }
}