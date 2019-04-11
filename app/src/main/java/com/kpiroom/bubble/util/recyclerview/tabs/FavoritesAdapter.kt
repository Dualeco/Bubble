package com.kpiroom.bubble.util.recyclerview.tabs

import android.view.LayoutInflater
import android.view.ViewGroup
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.TabItemFavoritesBinding
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.Comic
import com.kpiroom.bubble.util.recyclerview.core.CoreAdapter
import com.kpiroom.bubble.util.recyclerview.core.CoreHolder
import java.lang.ref.WeakReference

class FavoritesAdapter(
    data: List<Comic>,
    val onClick: (Comic) -> Unit
) : CoreAdapter<Comic, FavoritesHolder>(data) {

    override val viewType: Int = R.layout.tab_item_favorites

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesHolder =
        FavoritesHolder(
            TabItemFavoritesBinding.inflate(LayoutInflater.from(parent.context)),
            WeakReference(onClick)
        )

    override fun onViewRecycled(holder: FavoritesHolder) {
        super.onViewRecycled(holder)
        holder.apply {
            onClickWeak.clear()
        }
    }
}

class FavoritesHolder(
    val binding: TabItemFavoritesBinding,
    val onClickWeak: WeakReference<(Comic) -> Unit>
) : CoreHolder<Comic>(binding.root) {

    override fun bind(data: Comic) {
        itemData = data
        binding.itemName.text = data.title
    }

    init {
        itemView.setOnClickListener {
            onClickWeak.get()?.invoke(itemData)
        }
    }
}