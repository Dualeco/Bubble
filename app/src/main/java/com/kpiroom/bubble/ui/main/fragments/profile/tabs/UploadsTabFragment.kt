package com.kpiroom.bubble.ui.main.fragments.profile.tabs

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kpiroom.bubble.R
import com.kpiroom.bubble.util.recyclerview.TabListAdapter
import com.kpiroom.bubble.util.recyclerview.TabListHolder
import com.kpiroom.bubble.util.recyclerview.items.TabListItem
import kotlinx.android.synthetic.main.profile_tab_uploads.*

class UploadsTabFragment(uploadsAdapter: TabListAdapter) :
    CoreTabFragment<TabListItem, TabListHolder>(R.layout.profile_tab_uploads) {
    companion object {
        fun newInstance(
            adapter: TabListAdapter,
            tabTitle: String
        ) = UploadsTabFragment(adapter).apply {
            title = tabTitle
        }
    }

    override val adapter = uploadsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = uploads_recycler.apply {
            layoutManager = LinearLayoutManager(context)
        }
    }
}

