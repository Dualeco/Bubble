package com.kpiroom.bubble.ui.userPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.dichotome.profilebar.ui.tabPager.TabPagerAdapter
import com.dichotome.profileshared.extensions.isDisplayed
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.FragmentUserPageBinding
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.ui.comicPage.ComicPageFragment
import com.kpiroom.bubble.ui.profileTabs.SubscriptionsTabFragment
import com.kpiroom.bubble.ui.profileTabs.UploadsTabFragment
import com.kpiroom.bubble.ui.progress.ProgressFragment
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.livedata.observeNotNull
import kotlinx.android.synthetic.main.fragment_user_page.*
import kotlinx.android.synthetic.main.profile_tab_channels.*
import kotlinx.android.synthetic.main.profile_tab_user_uploads.*

class UserPageFragment : ProgressFragment<UserPageLogic, FragmentUserPageBinding>() {

    private lateinit var fragmentUploads: UploadsTabFragment
    private lateinit var fragmentChannels: SubscriptionsTabFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logic.user = arguments?.getParcelable(ARG_USER) ?: User()
        logic.initUserPage()

        logic.openedChannel.observeNotNull(this, Observer {
            findNavController().navigate(R.id.action_open_user_subscription, bundleOf(ARG_USER to it))
        })

        logic.openedComicPage.observeNotNull(this, Observer {
            findNavController().navigate(R.id.action_open_user_upload, bundleOf(ComicPageFragment.ARG_COMIC_PAGE to it))
        })

        fragmentUploads = UploadsTabFragment.newInstance(
            logic.uploadsAdapter,
            str(R.string.user_uploads),
            false
        )

        fragmentChannels = SubscriptionsTabFragment.newInstance(
            logic.channelsAdapter,
            str(R.string.user_channels)
        )

        logic.isSubscribed.observeNotNull(this, Observer {
            profileBar.isFollowed = it
        })

        logic.channelList.observeNotNull(this, Observer {
            channels_progress.isDisplayed = false
            channels_recycler.isDisplayed = it.isNotEmpty()
            fragmentChannels.items = it
        })

        logic.comicList.observeNotNull(this, Observer {
            uploads_progress.isDisplayed = false
            uploads_recycler.isDisplayed = it.isNotEmpty()
            fragmentUploads.items = it
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileBar.setupWithViewPager(profilePager)
        profilePager.adapter = TabPagerAdapter(childFragmentManager)

        profilePager.fragments = arrayListOf(
            fragmentUploads,
            fragmentChannels
        )
    }

    override fun provideLogic(): UserPageLogic = ViewModelProviders.of(this).get(UserPageLogic::class.java)

    override fun provideLayout(inflater: LayoutInflater, container: ViewGroup?): LayoutBuilder =
        LayoutBuilder(inflater, container, R.layout.fragment_user_page) {
            logic = this@UserPageFragment.logic
            user = this@UserPageFragment.logic.user
        }

    companion object {
        val ARG_USER = "user"
    }
}