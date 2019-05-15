package com.kpiroom.bubble.util.recyclerview.tabs

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dichotome.profilebar.util.extensions.download
import com.dichotome.profileshared.extensions.isDisplayed
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
        UploadsHolder(TabItemUploadsBinding.inflate(LayoutInflater.from(parent.context)), onRemove != null)

    override fun onViewRecycled(holder: UploadsHolder) {
        super.onViewRecycled(holder)
        Log.d("HHH", "Recycled ${onRemove}")
        holder.apply {
            onClickWeak?.clear()
            onRemoveWeak?.clear()
        }
    }

    override fun onBindViewHolder(holder: UploadsHolder, position: Int) =
        holder.bind(data[position], WeakReference(onClick), WeakReference(onRemove))
}

class UploadsHolder(val binding: TabItemUploadsBinding, val isButtonVisible: Boolean) :
    CoreHolder<Comic>(binding.root) {

    var onClickWeak: WeakReference<(Comic) -> Unit>? = null
    var onRemoveWeak: WeakReference<((Comic) -> Unit)?>? = null

    fun bind(data: Comic, onClick: WeakReference<(Comic) -> Unit>, onRemove: WeakReference<((Comic) -> Unit)?>) {
        Log.d("HHH", "${onRemove.get()}")

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
            if (isButtonVisible)
                setOnClickListener { v ->
                    onRemoveWeak?.get()?.invoke(itemData)
                }
            else
                isDisplayed = false
        }
    }
}