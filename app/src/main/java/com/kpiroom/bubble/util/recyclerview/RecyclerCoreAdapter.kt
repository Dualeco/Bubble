package com.kpiroom.bubble.util.recyclerview


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.dichotome.profilebar.util.extensions.download
import com.kpiroom.bubble.R
import com.kpiroom.bubble.util.recyclerview.items.TabCoreItem

abstract class TabCoreHolder<I : TabCoreItem>(
    parent: ViewGroup,
    viewType: Int
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
) {
    lateinit var id: String
    val nameTV = itemView.findViewById<TextView>(R.id.itemNameTV)
    val picView = itemView.findViewById<ImageView>(R.id.itemPic)

    fun bind(data: I) = data.run {
        id = uuid
        nameTV?.text = name
        picView?.download(picUrl, RequestOptions().apply {
            if (isPicCircular) circleCrop()
        })
        Unit
    }
}

abstract class TabCoreAdapter<I : TabCoreItem, VH : TabCoreHolder<I>>(
    var data: List<I> = listOf()
) : RecyclerView.Adapter<VH>() {

    abstract val itemViewType: Int

    override fun getItemCount() = data.size

    fun removeItem(index: Int) = updateTo(
        ArrayList(data).apply {
            removeAt(index)
        }
    )

    fun updateTo(newData: List<I>): Unit = DiffUtil.calculateDiff(
        TabDiffUtilCallback(data, newData)
    ).run {
        data = newData
        dispatchUpdatesTo(this@TabCoreAdapter)
    }
}