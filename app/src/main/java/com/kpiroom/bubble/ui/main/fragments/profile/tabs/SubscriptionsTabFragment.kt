package com.kpiroom.bubble.ui.main.fragments.profile.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dichotome.profilebar.ui.tabPager.TabFragment
import com.kpiroom.bubble.R
import kotlinx.android.synthetic.main.profile_tab_channels.*

class SubscriptionsTabFragment : TabFragment() {
    companion object {
        fun newInstance(tabTitle: String) = SubscriptionsTabFragment().apply {
            title = tabTitle
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(R.layout.profile_tab_channels, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = channels_recycler

        recyclerView.layoutManager = LinearLayoutManager(context)
    }
}