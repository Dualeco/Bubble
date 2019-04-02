package com.kpiroom.bubble.util.recyclerview

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kpiroom.bubble.R
import com.kpiroom.bubble.util.recyclerview.items.TabGridItem
import java.lang.ref.WeakReference

class TabGridAdapter(
    data: MutableList<TabGridItem>,
    override val itemViewType: Int,
    private val onHolderClick: ((Int) -> Unit)? = null
) : TabCoreAdapter<TabGridItem, TabGridHolder>(data) {

    override fun getItemViewType(position: Int) = itemViewType

    override fun onBindViewHolder(holder: TabGridHolder, position: Int) = holder.bind(
        data[position],
        onHolderClick
    )

    override fun onViewRecycled(holder: TabGridHolder) {
        super.onViewRecycled(holder)
        holder.holderClickListener.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TabGridHolder(parent, viewType)
}

class TabGridHolder(
    parent: ViewGroup,
    viewType: Int
) : TabCoreHolder<TabGridItem>(parent, viewType) {

    val infoTV = itemView.findViewById<TextView>(R.id.itemInfoTV)
    var holderClickListener: WeakReference<((Int) -> Unit)?> = WeakReference(null)

    fun bind(data: TabGridItem, onHolderClick: ((Int) -> Unit)?) {
        super.bind(data)
        holderClickListener = WeakReference(onHolderClick).apply {
            itemView.setOnClickListener {
                get()?.invoke(adapterPosition)
            }
        }


        infoTV?.text = data.info
    }
}