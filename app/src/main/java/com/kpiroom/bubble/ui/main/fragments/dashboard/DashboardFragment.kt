package com.kpiroom.bubble.ui.main.fragments.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.FragmentDashboardBinding
import com.kpiroom.bubble.ui.progress.ProgressFragment
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : ProgressFragment<DashboardLogic, FragmentDashboardBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allComicsRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = logic.adapter
        }

        logic.namedComics.observe(this, Observer {
           logic.updateAdapter(it)
        })
    }

    override fun provideLogic() = ViewModelProviders.of(this@DashboardFragment).get(DashboardLogic::class.java)

    override fun provideLayout(inflater: LayoutInflater, container: ViewGroup?) =
        LayoutBuilder(inflater, container, R.layout.fragment_dashboard) {
            logic = this@DashboardFragment.logic
        }
}