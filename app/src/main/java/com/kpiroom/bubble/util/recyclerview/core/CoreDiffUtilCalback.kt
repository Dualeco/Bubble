package com.kpiroom.bubble.util.recyclerview.core

import androidx.recyclerview.widget.DiffUtil
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.Comparable


class TabDiffUtilCallback<I : Comparable>(
    private val oldList: List<I>,
    private val newList: List<I>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].name == newList[newItemPosition].name
}