package com.kpiroom.bubble.util.recyclerview.tabs

import android.view.LayoutInflater
import android.view.View
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
    val onUnfollowed: ((User) -> Boolean)? = null
) : CoreAdapter<User, SubscriptionHolder>(data) {

    override val viewType: Int = R.layout.tab_item_channels

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionHolder =
        SubscriptionHolder(TabItemChannelsBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onViewRecycled(holder: SubscriptionHolder) {
        super.onViewRecycled(holder)
        holder.apply {
            onClickWeak?.clear()
            onUnfollowedWeak?.clear()
        }
    }

    override fun onBindViewHolder(holder: SubscriptionHolder, position: Int) =
        holder.bind(data[position], WeakReference(onClick), WeakReference(onUnfollowed))
}

class SubscriptionHolder(val binding: TabItemChannelsBinding) : CoreHolder<User>(binding.root) {

    var onClickWeak: WeakReference<(User) -> Unit>? = null
    var onUnfollowedWeak: WeakReference<((User) -> Boolean)?>? = null

    private var unfollowed = false
        set(value) {
            field = value
            toggleUnfollowIcon(value)
        }

    fun bind(data: User, onClick: WeakReference<(User) -> Unit>, onUnfollowed: WeakReference<((User) -> Boolean)?>) {
        onClickWeak = onClick
        onUnfollowedWeak = onUnfollowed

        itemData = data
        binding.apply {
            itemName.text = data.name
            itemPic.download(data.photoUrl)
        }
    }

    private fun toggleUnfollowIcon(unfollowed: Boolean) = binding.run {
        unfollowButton.setImageResource(
            if (unfollowed)
                R.drawable.ic_notifications_none_grey
            else
                R.drawable.ic_notifications_active_grey
        )
    }

    init {
        toggleUnfollowIcon(unfollowed)
        itemView.setOnClickListener {
            onClickWeak?.get()?.invoke(itemData)
        }
        binding.unfollowButton.apply {
            onUnfollowedWeak?.get()?.let {
                setOnClickListener { v ->
                    unfollowed = it.invoke(itemData)
                }
            } ?: run {
                visibility = View.GONE
            }
        }
    }


}