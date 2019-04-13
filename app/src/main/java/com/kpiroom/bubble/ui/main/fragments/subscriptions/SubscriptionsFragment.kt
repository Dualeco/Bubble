package com.kpiroom.bubble.ui.main.fragments.subscriptions

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
import com.kpiroom.bubble.databinding.FragmentSubscriptionBinding
import com.kpiroom.bubble.ui.comicPage.ComicPageFragment
import com.kpiroom.bubble.ui.progress.ProgressFragment
import com.kpiroom.bubble.util.livedata.observeNotNull
import kotlinx.android.synthetic.main.fragment_subscription.*

class SubscriptionsFragment : ProgressFragment<SubscriptionsLogic, FragmentSubscriptionBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logic.clickedComic.observeNotNull(this, Observer {
            findNavController().navigate(
                R.id.action_open_subscription_comic,
                bundleOf(ComicPageFragment.ARG_COMIC_PAGE to it)
            )
        })

        logic.comicPages.observe(this, Observer {
            subscriptionsProgress.isDisplayed = false
            subscriptionsRecycler.isDisplayed = it.isNotEmpty()
            logic.updateAdapter(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscriptionsRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = logic.adapter
        }
    }

    override fun provideLogic() = ViewModelProviders.of(this).get(SubscriptionsLogic::class.java)

    override fun provideLayout(inflater: LayoutInflater, container: ViewGroup?) =
        LayoutBuilder(inflater, container, R.layout.fragment_subscription) {
            logic = this@SubscriptionsFragment.logic
        }
}