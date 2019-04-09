package com.kpiroom.bubble.util.recyclerview

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.kpiroom.bubble.R
import com.kpiroom.bubble.util.recyclerview.items.TabListItem
import java.lang.ref.WeakReference

class TabListAdapter(
    data: MutableList<TabListItem>,
    override val itemViewType: Int,
    val onViewClick: ((String) -> Unit)? = null,
    val onActionClick: ((String) -> Unit)? = null
) : TabCoreAdapter<TabListItem, TabListHolder>(data) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TabListHolder(parent, itemViewType)

    override fun onBindViewHolder(holder: TabListHolder, position: Int) = holder.bind(
        data[position],
        onViewClick,
        onActionClick
    )

    override fun onViewRecycled(holder: TabListHolder) {
        super.onViewRecycled(holder)
        holder.apply {
            holderClickListener.clear()
            actionClickListener.clear()
        }
    }
}

class TabListHolder(
    parent: ViewGroup,
    viewType: Int
) : TabCoreHolder<TabListItem>(parent, viewType) {
    val actionButton = itemView.findViewById<ImageButton>(R.id.action_button)

    var holderClickListener: WeakReference<((String) -> Unit)?> = WeakReference(null)
    var actionClickListener: WeakReference<((String) -> Unit)?> = WeakReference(null)

    fun bind(
        data: TabListItem,
        onHolderClick: ((String) -> Unit)?,
        onActionClick: ((String) -> Unit)?
    ) {
        super.bind(data)
        holderClickListener = WeakReference(onHolderClick).apply {
            itemView.setOnClickListener {
                get()?.invoke(id)
            }
        }
        actionClickListener = WeakReference(onActionClick).apply {
            actionButton?.setOnClickListener {
                get()?.invoke(id)
            }
        }
    }
}