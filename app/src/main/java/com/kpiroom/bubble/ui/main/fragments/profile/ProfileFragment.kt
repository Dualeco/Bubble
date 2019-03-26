package com.kpiroom.bubble.ui.main.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.FragmentProfileBinding
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.ui.core.CoreFragment
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : CoreFragment<ProfileLogic, FragmentProfileBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Source.userPrefs.apply {
            profileTV.text = "$uuid $username $joinedDate $isPhotoSet $isWallpaperSet"
        }
    }

    override fun provideLogic() = ViewModelProviders.of(this).get(ProfileLogic::class.java)

    override fun provideLayout(inflater: LayoutInflater, container: ViewGroup?) =
        LayoutBuilder(inflater, container, R.layout.fragment_profile) {
            logic = this@ProfileFragment.logic
        }
}