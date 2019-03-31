package com.kpiroom.bubble.ui.main.fragments.profile.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dichotome.profilebar.stubs.FavListItem
import com.dichotome.profilebar.stubs.TabListAdapter
import com.dichotome.profilebar.stubs.TabListItem
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
        items = listOf(
            FavListItem(
                "The Amazing Spider-Man",
                "#5, 2018",
                "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/amazing-spider-man--2-cover-art-by-ryan-ottley.png?1384968217"
            ),
            FavListItem(
                "Star Wars",
                "#55, 2008",
                "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/star-wars--55-cover-art-by-david-marquez.png?1384968217"
            ),
            FavListItem(
                "Esteemed comic book author",
                "#214, 2017",
                "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/amazing-spider-man--2-cover-art-by-ryan-ottley.png?1384968217"
            ),
            FavListItem(
                "Superior comic book writer",
                "#3, 2019",
                "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/star-wars--55-cover-art-by-david-marquez.png?1384968217"
            ),
            FavListItem(
                "Batman: Rebirth",
                "#4, 2011",
                "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccovers2017/large/batman26-mikeljanin.png?1384968217"
            ),
            FavListItem(
                "The Amazing Spider-Man",
                "#5, 2018",
                "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/amazing-spider-man--2-cover-art-by-ryan-ottley.png?1384968217"
            ),
            FavListItem(
                "Star Wars",
                "#55, 2008",
                "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/star-wars--55-cover-art-by-david-marquez.png?1384968217"
            ),
            FavListItem(
                "Esteemed comic book author",
                "#214, 2017",
                "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/amazing-spider-man--2-cover-art-by-ryan-ottley.png?1384968217"
            ),
            FavListItem(
                "Superior comic book writer",
                "#3, 2019",
                "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/star-wars--55-cover-art-by-david-marquez.png?1384968217"
            ),
            FavListItem(
                "Batman: Rebirth",
                "#4, 2011",
                "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccovers2017/large/batman26-mikeljanin.png?1384968217"
            )
        )
    }
}