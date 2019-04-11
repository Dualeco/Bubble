package com.kpiroom.bubble.util.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dichotome.profilebar.util.extensions.download
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.ItemAllComicsBinding
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.Comic
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.date.msToDateStr
import com.kpiroom.bubble.util.recyclerview.core.CoreAdapter
import com.kpiroom.bubble.util.recyclerview.core.CoreHolder
import com.kpiroom.bubble.util.recyclerview.model.NamedComic
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

class AllComicsAdapter(
    data: List<NamedComic>,
    val onClick: (NamedComic) -> Unit
) : CoreAdapter<NamedComic, AllComicsHolder>(data) {

    override val viewType: Int = R.layout.item_all_comics

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllComicsHolder =
        AllComicsHolder(
            ItemAllComicsBinding.inflate(LayoutInflater.from(parent.context)),
            WeakReference(onClick)
        )

    override fun onViewRecycled(holder: AllComicsHolder) {
        super.onViewRecycled(holder)
        holder.apply {
            onClickWeak.clear()
        }
    }
}

class AllComicsHolder(
    val binding: ItemAllComicsBinding,
    val onClickWeak: WeakReference<(NamedComic) -> Unit>
) : CoreHolder<NamedComic>(binding.root) {

    override fun bind(data: NamedComic) = binding.run {
        itemData = data
        itemTitle.text = data.title
        itemThumbnail.download(data.thumbnailUrl)
        itemDownloadCount.text = data.downloads.toString()
        itemStarCount.text = data.stars.toString()
        itemAuthorName.text = data.authorName
        itemUploadDate.text = msToDateStr(data.uploadTimeMs)
    }

    init {
        itemView.setOnClickListener {
            onClickWeak.get()?.invoke(itemData)
        }
    }
}

