package com.kpiroom.bubble.util.recyclerview.tabs

import android.view.LayoutInflater
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
    val onRemove: (Comic) -> Unit
) : CoreAdapter<Comic, UploadsHolder>(data) {

    override val viewType: Int = R.layout.tab_item_uploads

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadsHolder =
        UploadsHolder(
            TabItemUploadsBinding.inflate(LayoutInflater.from(parent.context)),
            WeakReference(onClick),
            WeakReference(onRemove)
        )

    override fun onViewRecycled(holder: UploadsHolder) {
        super.onViewRecycled(holder)
        holder.apply {
            onClickWeak.clear()
            onRemoveWeak.clear()
        }
    }
}

class UploadsHolder(
    val binding: TabItemUploadsBinding,
    val onClickWeak: WeakReference<(Comic) -> Unit>,
    val onRemoveWeak: WeakReference<(Comic) -> Unit>
) : CoreHolder<Comic>(binding.root) {

    override fun bind(data: Comic) {
        itemData = data
        binding.itemName.text = data.title
        binding.itemPic.download(data.thumbnailUrl)
    }

    init {
        itemView.setOnClickListener {
            onClickWeak.get()?.invoke(itemData)
        }
        binding.removeButton.setOnClickListener {
            onRemoveWeak.get()?.invoke(itemData)
        }
    }
}