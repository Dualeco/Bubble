package com.kpiroom.bubble.ui.profileTabs

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.TabItemChannelsBinding
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.util.recyclerview.tabs.SubscriptionAdapter
import com.kpiroom.bubble.util.recyclerview.tabs.SubscriptionHolder
import kotlinx.android.synthetic.main.profile_tab_channels.*

class SubscriptionsTabFragment(channelsAdapter: SubscriptionAdapter) :
    CoreTabFragment<User, SubscriptionHolder>(R.layout.profile_tab_channels) {
    companion object {
        fun newInstance(
            adapter: SubscriptionAdapter,
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