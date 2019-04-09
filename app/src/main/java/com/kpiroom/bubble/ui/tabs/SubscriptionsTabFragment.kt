package com.kpiroom.bubble.ui.tabs

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kpiroom.bubble.R
import com.kpiroom.bubble.util.recyclerview.TabListAdapter
import com.kpiroom.bubble.util.recyclerview.TabListHolder
import com.kpiroom.bubble.util.recyclerview.items.TabListItem
import kotlinx.android.synthetic.main.profile_tab_channels.*

class SubscriptionsTabFragment(channelsAdapter: TabListAdapter) :
    CoreTabFragment<TabListItem, TabListHolder>(R.layout.profile_tab_channels) {
    companion object {
        fun newInstance(
            adapter: TabListAdapter,
            tabTitle: String
        ) = SubscriptionsTabFragment(adapter).apply {
            title = tabTitle
        }
    }

    override val adapter = channelsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = channels_recycler.apply {
            layoutManager = LinearLayoutManager(context)
        }
    }
}