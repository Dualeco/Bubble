package com.kpiroom.bubble.ui.main.fragments.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dichotome.profilebar.ui.tabPager.TabPagerAdapter
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.FragmentProfileBinding
import com.kpiroom.bubble.ui.accountSetup.AccountSetupActivity
import com.kpiroom.bubble.ui.core.CoreFragment
import com.kpiroom.bubble.ui.login.LoginActivity
import com.kpiroom.bubble.ui.main.MainActivity
import com.kpiroom.bubble.ui.tabs.FavouritesTabFragment
import com.kpiroom.bubble.ui.tabs.SubscriptionsTabFragment
import com.kpiroom.bubble.ui.tabs.UploadsTabFragment
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.imageUpload.createCameraPictureUri
import com.kpiroom.bubble.util.imageUpload.startImageSelectionActivity
import com.kpiroom.bubble.util.livedata.*
import com.kpiroom.bubble.util.progressState.ProgressState
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : CoreFragment<ProfileLogic, FragmentProfileBinding>() {

    private var photoCaptureUri: Uri? = null
    private var usingCamera = false

    private lateinit var fragmentChannels: SubscriptionsTabFragment
    private lateinit var fragmentFavorites: FavouritesTabFragment
    private lateinit var fragmentUploads: UploadsTabFragment

    private lateinit var parentProgressState: MediatorLiveData<ProgressState>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentProgressState = (activity as MainActivity).provideLogic().progress
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentProgressState.addSource(logic.progress)

        logic.updateProfileImages()

        logic.uploadsLiveData.observeResource(this) { list, _ ->
            list?.forEach {
                Log.d(TAG, it)
            }
        }

        fragmentChannels = SubscriptionsTabFragment.newInstance(
            logic.channelsAdapter,
            str(R.string.profile_channels)
        )
        fragmentFavorites = FavouritesTabFragment.newInstance(
            logic.favoritesAdapter,
            str(R.string.profile_favorites)
        )
        fragmentUploads = UploadsTabFragment.newInstance(
            logic.uploadsAdapter,
            str(R.string.profile_uploads)
        )

        logic.loggedOut.observeTrue(this, Observer {
            context?.let {
                activity?.finish()
                startActivity(LoginActivity.getIntent(it))
            }
        })

        logic.optionWindowClicked.observeTrue(this, Observer {
            profileBar.optionWindow.dismiss()
        })

        logic.useCameraForPhoto.observeNotNull(this, Observer {
            usingCamera = it
            addPhoto(it)
        })

        logic.restoreFocus.observeTrue(this, Observer {
            profileBar.editTitle.requestFocus()
        })

        logic.channelList.observeNotNull(this, Observer {
            fragmentChannels.items = it
        })

        logic.favoriteList.observeNotNull(this, Observer {
            fragmentFavorites.items = it
        })

        logic.uploadList.observeNotNull(this, Observer {
            fragmentUploads.items = it
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profilePager.adapter = TabPagerAdapter(childFragmentManager)
        profileBar.setupWithViewPager(profilePager)

        profilePager.fragments = arrayListOf(
            fragmentChannels,
            fragmentFavorites,
            fragmentUploads
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (resultCode == Activity.RESULT_OK)
            if (requestCode == AccountSetupActivity.REQUEST_PHOTO)
                logic.apply {
                    val uri = if (usingCamera)
                        photoCaptureUri
                    else
                        intent?.data

                    uri?.let {
                        val isProfilePhoto = photoChangeRequested.value == true
                        dispatchUri(it, isProfilePhoto)
                    }
                }
    }

    private fun addPhoto(useCamera: Boolean) {
        context?.also {
            photoCaptureUri =
                if (useCamera)
                    createCameraPictureUri(it)
                else
                    null
        }
        startImageSelectionActivity(this, useCamera, photoCaptureUri)
    }

    override fun onPause() {
        super.onPause()
        logic.isTitleEditable.value = false
    }

    override fun onDetach() {
        super.onDetach()
        parentProgressState.removeSource(logic.progress)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logic.progress.value = null
    }

    override fun provideLogic() = ViewModelProviders.of(this).get(ProfileLogic::class.java)

    override fun provideLayout(inflater: LayoutInflater, container: ViewGroup?) =
        LayoutBuilder(inflater, container, R.layout.fragment_profile) {
            logic = this@ProfileFragment.logic
        }

    companion object {
        private val TAG = "ProfileFragment"
    }
}