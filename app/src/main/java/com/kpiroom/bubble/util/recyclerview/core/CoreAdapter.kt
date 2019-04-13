package com.kpiroom.bubble.util.recyclerview.core

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.Comparable

abstract class CoreAdapter<T : Comparable, VH : CoreHolder<T>>(
    data: List<T>
) : RecyclerView.Adapter<VH>() {

    var data: List<T> = data
        private set

    abstract val viewType: Int
    override fun getItemViewType(position: Int): Int = viewType

    override fun getItemCount(): Int = data.size

    fun updateData(newData: List<T>) = DiffUtil.calculateDiff(
        TabDiffUtilCallback(data, newData)
    ).run {
        data = newData
        dispatchUpdatesTo(this@CoreAdapter)
    }
}

abstract class CoreHolder<T : Comparable>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    lateinit var itemData: T
}