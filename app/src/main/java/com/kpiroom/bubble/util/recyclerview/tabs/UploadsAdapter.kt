package com.kpiroom.bubble.util.recyclerview.tabs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dichotome.profilebar.util.extensions.download
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.TabItemUploadsBinding
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.Comic
import com.kpiroom.bubble.util.recyclerview.core.CoreAdapter
import com.kpiroom.bubble.util.recyclerview.core.CoreHolder
import java.lang.ref.WeakReference

class UploadsAdapter(
    data: List<Comic>,
    val onClick: (Comic) -> Unit,
    val onRemove: ((Comic) -> Unit)? = null
) : CoreAdapter<Comic, UploadsHolder>(data) {

    override val viewType: Int = R.layout.tab_item_uploads

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadsHolder =
        UploadsHolder(TabItemUploadsBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onViewRecycled(holder: UploadsHolder) {
        super.onViewRecycled(holder)
        holder.apply {
            onClickWeak?.clear()
            onRemoveWeak?.clear()
        }
    }

    override fun onBindViewHolder(holder: UploadsHolder, position: Int) =
        holder.bind(data[position], WeakReference(onClick), WeakReference(onRemove))
}

class UploadsHolder(val binding: TabItemUploadsBinding) : CoreHolder<Comic>(binding.root) {

    var onClickWeak: WeakReference<(Comic) -> Unit>? = null
    var onRemoveWeak: WeakReference<((Comic) -> Unit)?>? = null

    fun bind(data: Comic, onClick: WeakReference<(Comic) -> Unit>, onRemove: WeakReference<((Comic) -> Unit)?>) {
        onClickWeak = onClick
        onRemoveWeak = onRemove

        itemData = data
        binding.itemName.text = data.name
        binding.itemPic.download(data.thumbnailUrl)
    }

    init {
        itemView.setOnClickListener {
            onClickWeak?.get()?.invoke(itemData)
        }

        binding.removeButton.apply {
            onRemoveWeak?.get()?.let {
                setOnClickListener { v -> it.invoke(itemData) }
            } ?: run {
                visibility = View.GONE
            }
        }
    }
}