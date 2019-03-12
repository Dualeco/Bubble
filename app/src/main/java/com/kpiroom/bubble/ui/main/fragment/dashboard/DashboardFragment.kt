package com.kpiroom.bubble.ui.main.fragment.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.kpiroom.bubble.databinding.FragmentDashboardBinding
import com.kpiroom.bubble.ui.core.CoreFragment
import com.kpiroom.bubble.R

class DashboardFragment : CoreFragment<DashboardLogic, FragmentDashboardBinding>() {

    override fun provideLogic() = ViewModelProviders.of(this@DashboardFragment).get(DashboardLogic::class.java)

    override fun provideLayout(inflater: LayoutInflater, container: ViewGroup?) = LayoutBuilder(inflater, container, R.layout.fragment_dashboard) {
        logic = this@DashboardFragment.logic
    }
}