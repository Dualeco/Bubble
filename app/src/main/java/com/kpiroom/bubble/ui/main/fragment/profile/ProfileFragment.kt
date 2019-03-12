package com.kpiroom.bubble.ui.main.fragment.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.kpiroom.bubble.databinding.FragmentProfileBinding
import com.kpiroom.bubble.ui.core.CoreFragment
import com.kpiroom.bubble.R

class ProfileFragment : CoreFragment<ProfileLogic, FragmentProfileBinding>() {

    override fun provideLogic() = ViewModelProviders.of(this).get(ProfileLogic::class.java)

    override fun provideLayout(inflater: LayoutInflater, container: ViewGroup?) = LayoutBuilder(inflater, container, R.layout.fragment_profile) {
        logic = this@ProfileFragment.logic
    }
}