package com.kpiroom.bubble.util.recyclerview.tabs

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dichotome.profilebar.util.extensions.download
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.TabItemFavoritesBinding
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.Comic
import com.kpiroom.bubble.util.date.msToDateStr
import com.kpiroom.bubble.util.recyclerview.core.CoreAdapter
import com.kpiroom.bubble.util.recyclerview.core.CoreHolder
import java.lang.ref.WeakReference

class FavoritesAdapter(
    data: List<Comic>,
    val onClick: (Comic) -> Unit
) : CoreAdapter<Comic, FavoritesHolder>(data) {

    override val viewType: Int = R.layout.tab_item_favorites

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesHolder =
        FavoritesHolder(TabItemFavoritesBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onViewRecycled(holder: FavoritesHolder) {
        super.onViewRecycled(holder)
        holder.apply {
            onClickWeak?.clear()
        }
    }

    override fun onBindViewHolder(holder: FavoritesHolder, position: Int) =
        holder.bind(data[position], WeakReference(onClick))
}

class FavoritesHolder(val binding: TabItemFavoritesBinding) : CoreHolder<Comic>(binding.root) {

    var onClickWeak: WeakReference<(Comic) -> Unit>? = null

    fun bind(data: Comic, onClick: WeakReference<(Comic) -> Unit>) {
        itemData = data

        onClickWeak = onClick
        binding.apply {
            itemName.text = data.name
            itemInfoTV.text = msToDateStr(data.uploadTimeMs)
            itemPic.download(if (data.favPreviewUrl.isBlank()) data.previewUrl else data.favPreviewUrl)
        }
    }

    init {
        itemView.setOnClickListener {
            onClickWeak?.get()?.invoke(itemData)
        }
    }
}