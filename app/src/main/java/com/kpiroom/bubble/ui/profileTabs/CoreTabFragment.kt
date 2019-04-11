package com.kpiroom.bubble.ui.profileTabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.dichotome.profilebar.ui.tabPager.TabFragment
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.Comparable
import com.kpiroom.bubble.util.recyclerview.core.CoreAdapter
import com.kpiroom.bubble.util.recyclerview.core.CoreHolder

abstract class CoreTabFragment<T : Comparable, VH : CoreHolder<T>>(
    private val layoutId: Int
) : TabFragment() {

    abstract val adapter: CoreAdapter<T, VH>

    var items: List<T>
        get() = adapter.data
        set(value) = adapter.updateData(value)

    var recyclerView: RecyclerView? = null
        set(value) {
            field = value?.also {
                it.adapter = adapter
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(layoutId, container, false)
}