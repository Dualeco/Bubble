package com.kpiroom.bubble.ui.userPage

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.Comic
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.ui.progress.ProgressFragmentLogic
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.collections.*
import com.kpiroom.bubble.util.livedata.addSourceNotNull
import com.kpiroom.bubble.util.livedata.progressState.alertAsync
import com.kpiroom.bubble.util.recyclerview.model.ComicPage
import com.kpiroom.bubble.util.recyclerview.tabs.SubscriptionAdapter
import com.kpiroom.bubble.util.recyclerview.tabs.UploadsAdapter

class UserPageLogic() : ProgressFragmentLogic() {

    lateinit var user: User

    var comicList = MediatorLiveData<List<Comic>>()
    var channelList = MutableLiveData<List<User>>()

    val isSubscribed = MutableLiveData<Boolean>()

    val openedChannel = MutableLiveData<User>()
    val openedComicPage = MutableLiveData<ComicPage>()

    val channelsAdapter: SubscriptionAdapter = SubscriptionAdapter(
        listOf(),
        ::onChannelClicked
    )

    val uploadsAdapter: UploadsAdapter = UploadsAdapter(
        listOf(),
        ::onUploadClicked
    )

    private fun initComicList() {
        val comicIds = Source.run { api.getUserComicIds(user.id) }
        comicList = MediatorLiveData<List<Comic>>().apply {
            addSourceNotNull(comicIds) { list ->
                runAsync {
                    list.mapAsync(bag) { Source.api.getComicData(it) }
                        .sortedByDescending { it.uploadTimeMs }
                        .also { postValue(it) }
                }
            }
        }
    }

    private fun initChannelList() {
        runAsync {
            Source.run {
                api.getUserSubscriptions(user.id).toTimeRecordList()
                    .mapDataAsync(bag) { Source.api.getUserData(it) }
                    .sortedByLatest()
                    .entries()
                    .also { channelList.postValue(it) }
            }
        }
    }

    fun onChannelClicked(user: User) = clickThrottler.next {
        openedChannel.value = user
    }

    fun onUploadClicked(comic: Comic) = clickThrottler.next {
        runAsync {
            openedComicPage.postValue(
                ComicPage(
                    comic,
                    Source.api.getUserData(comic.authorId) ?: User()
                )
            )
        }
    }

    fun onFollowed() = clickThrottler.next {
        isSubscribed.value = !(isSubscribed.value ?: false)
        runAsync {
            Source.apply {
                if (isSubscribed.value == true)
                    api.unsubscribeFrom(userPrefs.uuid, user.id)
                else
                    api.subscribeTo(userPrefs.uuid, user.id)
            }
        }
    }

    fun initUserPage() {
        initSubscription()
        initComicList()
        initChannelList()
    }

    private fun initSubscription() = runAsync {
        Source.apply {
            isSubscribed.postValue(api.userIsSubscribedTo(userPrefs.uuid, user.id))
        }
    }

    fun runAsync(action: suspend () -> Any) {
        AsyncProcessor {
            action()
        } handleError {
            progress.alertAsync(it.message)
        } runWith (bag)
    }
}