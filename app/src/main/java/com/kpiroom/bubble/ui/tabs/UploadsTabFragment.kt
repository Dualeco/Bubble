package com.kpiroom.bubble.ui.tabs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kpiroom.bubble.R
import com.kpiroom.bubble.util.recyclerview.TabListAdapter
import com.kpiroom.bubble.util.recyclerview.TabListHolder
import com.kpiroom.bubble.util.recyclerview.items.TabListItem
import kotlinx.android.synthetic.main.profile_tab_uploads.*

class UploadsTabFragment(uploadsAdapter: TabListAdapter) :
    CoreTabFragment<TabListItem, TabListHolder>(R.layout.profile_tab_uploads) {
    companion object {
        val UPLOAD_URI = "uploadUri"
        private val REQUEST_UPLOAD = 0
        private val CONTENT_TYPE = "application/pdf"

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

        upload_fab.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).apply {
                type = CONTENT_TYPE
            }.also {
                startActivityForResult(it, REQUEST_UPLOAD)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK)
            if (requestCode == REQUEST_UPLOAD)
                data?.data.let { uri ->
                    findNavController().navigate(
                        R.id.action_upload_comic,
                        bundleOf(UPLOAD_URI to uri.toString())
                    )
                }
    }
}

