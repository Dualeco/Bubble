package com.kpiroom.bubble.util.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.kpiroom.bubble.util.recyclerview.items.TabCoreItem


class TabDiffUtilCallback<I : TabCoreItem>(
    private val oldList: List<I>,
    private val newList: List<I>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].uuid == newList[newItemPosition].uuid

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].name == newList[newItemPosition].name

}