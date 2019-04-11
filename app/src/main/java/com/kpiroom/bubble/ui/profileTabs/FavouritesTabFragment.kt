package com.kpiroom.bubble.ui.profileTabs

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.TabItemFavoritesBinding
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.Comic
import com.kpiroom.bubble.util.recyclerview.tabs.FavoritesAdapter
import com.kpiroom.bubble.util.recyclerview.tabs.FavoritesHolder
import kotlinx.android.synthetic.main.profile_tab_favorites.*

class FavouritesTabFragment(favoritesAdapter: FavoritesAdapter) :
    CoreTabFragment<Comic, FavoritesHolder>(R.layout.profile_tab_favorites) {

    companion object {
        fun newInstance(
            adapter: FavoritesAdapter,
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