package com.kpiroom.bubble.ui.main.fragments.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dichotome.profilebar.ui.tabPager.TabPagerAdapter
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.FragmentProfileBinding
import com.kpiroom.bubble.ui.accountSetup.AccountSetupActivity
import com.kpiroom.bubble.ui.core.CoreFragment
import com.kpiroom.bubble.ui.login.LoginActivity
import com.kpiroom.bubble.util.imageSelection.createCameraPictureUri
import com.kpiroom.bubble.util.imageSelection.startImageSelectionActivity
import com.kpiroom.bubble.util.livedata.observeTrue
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : CoreFragment<ProfileLogic, FragmentProfileBinding>() {

    private var photoCaptureUri: Uri? = null
    private var usingCamera = false

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logic.updateProfileImages()

        logic.loggedOut.observeTrue(this, Observer {
            context?.let {
                activity?.finish()
                startActivity(LoginActivity.getIntent(it))
            }
        })

        logic.optionWindowClicked.observeTrue(this, Observer {
            profileBar.optionWindow.dismiss()
        })

        logic.useCameraForPhoto.observe(this, Observer {
            it?.let { isCamera ->
                usingCamera = isCamera
                addPhoto(isCamera)
            }
        })
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profilePager.adapter = TabPagerAdapter(childFragmentManager)
        profileBar.setupWithViewPager(profilePager)
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
}