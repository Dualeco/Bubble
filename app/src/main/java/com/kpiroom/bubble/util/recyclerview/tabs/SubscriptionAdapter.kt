package com.kpiroom.bubble.util.recyclerview.tabs

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dichotome.profilebar.util.extensions.download
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.TabItemChannelsBinding
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.util.recyclerview.core.CoreAdapter
import com.kpiroom.bubble.util.recyclerview.core.CoreHolder
import java.lang.ref.WeakReference

class SubscriptionAdapter(
    data: List<User>,
    val onClick: (User) -> Unit,
    val onUnsubscribe: (User) -> Unit
) : CoreAdapter<User, SubscriptionHolder>(data) {

    override val viewType: Int = R.layout.tab_item_channels

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionHolder =
        SubscriptionHolder(
            TabItemChannelsBinding.inflate(LayoutInflater.from(parent.context)),
            WeakReference(onClick),
            WeakReference(onUnsubscribe)
        )

    override fun onViewRecycled(holder: SubscriptionHolder) {
        super.onViewRecycled(holder)
        holder.apply {
            onClickWeak.clear()
            onUnsubscribeWeak.clear()
        }
    }
}

class SubscriptionHolder(
    val binding: TabItemChannelsBinding,
    val onClickWeak: WeakReference<(User) -> Unit>,
    val onUnsubscribeWeak: WeakReference<(User) -> Unit>
) : CoreHolder<User>(binding.root) {

    override fun bind(data: User) {
        itemData = data
        binding.itemName.text = data.name
        binding.itemPic.download(data.photoName)
    }

    init {
        itemView.setOnClickListener {
            onClickWeak.get()?.invoke(itemData)
        }
        binding.unsubscribeButton.setOnClickListener {
            onUnsubscribeWeak.get()?.invoke(itemData)
        }
    }
}