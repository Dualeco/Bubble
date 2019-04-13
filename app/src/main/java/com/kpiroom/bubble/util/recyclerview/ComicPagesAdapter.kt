package com.kpiroom.bubble.util.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dichotome.profilebar.util.extensions.download
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.ItemAllComicsBinding
import com.kpiroom.bubble.util.date.msToDateStr
import com.kpiroom.bubble.util.recyclerview.core.CoreAdapter
import com.kpiroom.bubble.util.recyclerview.core.CoreHolder
import com.kpiroom.bubble.util.recyclerview.model.ComicPage
import java.lang.ref.WeakReference

class ComicPagesAdapter(
    data: List<ComicPage>,
    val onClick: (ComicPage) -> Unit
) : CoreAdapter<ComicPage, AllComicsHolder>(data) {

    override val viewType: Int = R.layout.item_all_comics

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllComicsHolder =
        AllComicsHolder(
            ItemAllComicsBinding.inflate(LayoutInflater.from(parent.context))
        )

    override fun onViewRecycled(holder: AllComicsHolder) {
        super.onViewRecycled(holder)
        holder.apply {
            onClickWeak?.clear()
        }
    }

    override fun onBindViewHolder(holder: AllComicsHolder, position: Int) =
        holder.bind(data[position], WeakReference(onClick))
}

class AllComicsHolder(val binding: ItemAllComicsBinding) : CoreHolder<ComicPage>(binding.root) {

    var onClickWeak: WeakReference<(ComicPage) -> Unit>? = null

    fun bind(data: ComicPage, onClick: WeakReference<(ComicPage) -> Unit>) = binding.run {
        onClickWeak = onClick

        itemData = data
        itemTitle.text = data.name
        itemThumbnail.download(data.thumbnailUrl)
        itemDownloadCount.text = data.downloads.toString()
        itemStarCount.text = data.stars.toString()
        itemAuthorName.text = data.authorName
        itemUploadDate.text = msToDateStr(data.uploadTimeMs)
    }

    init {
        itemView.setOnClickListener {
            onClickWeak?.get()?.invoke(itemData)
        }
    }
}

