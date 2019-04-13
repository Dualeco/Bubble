package com.kpiroom.bubble.ui.main.fragments.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dichotome.profileshared.extensions.isDisplayed
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.FragmentCommonBinding
import com.kpiroom.bubble.ui.comicPage.ComicPageFragment.Companion.ARG_COMIC_PAGE
import com.kpiroom.bubble.ui.progress.ProgressFragment
import com.kpiroom.bubble.util.livedata.observeNotNull
import kotlinx.android.synthetic.main.fragment_common.*

class CommonFragment : ProgressFragment<CommonLogic, FragmentCommonBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logic.clickedComic.observeNotNull(this, Observer {
            findNavController().navigate(R.id.action_open_common_comic, bundleOf(ARG_COMIC_PAGE to it))
        })

        logic.comicPages.observe(this, Observer {
            commonProgress.isDisplayed = false
            commonRecycler.isDisplayed = it.isNotEmpty()
            logic.updateAdapter(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        commonRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = logic.adapter
        }
    }

    override fun provideLogic() = ViewModelProviders.of(this@CommonFragment).get(CommonLogic::class.java)

    override fun provideLayout(inflater: LayoutInflater, container: ViewGroup?) =
        LayoutBuilder(inflater, container, R.layout.fragment_common) {
            logic = this@CommonFragment.logic
        }
}