package com.kpiroom.bubble.ui.main.fragment.subscription

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.kpiroom.bubble.databinding.FragmentSubscriptionBinding
import com.kpiroom.bubble.ui.core.CoreFragment
import com.kpiroom.bubble.R

class SubscriptionFragment : CoreFragment<SubscriptionLogic, FragmentSubscriptionBinding>() {

    override fun provideLogic() = ViewModelProviders.of(this).get(SubscriptionLogic::class.java)

    override fun provideLayout(inflater: LayoutInflater, container: ViewGroup?) = LayoutBuilder(inflater, container, R.layout.fragment_subscription) {
        logic = this@SubscriptionFragment.logic
    }
}