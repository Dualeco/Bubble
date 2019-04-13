package com.kpiroom.bubble.ui.main.fragments.subscriptions

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.Comic
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.ui.progress.ProgressFragmentLogic
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.collections.mapAsync
import com.kpiroom.bubble.util.livedata.addSource
import com.kpiroom.bubble.util.livedata.addSourceNotNull
import com.kpiroom.bubble.util.livedata.progressState.alertAsync
import com.kpiroom.bubble.util.recyclerview.ComicPagesAdapter
import com.kpiroom.bubble.util.recyclerview.model.ComicPage

class SubscriptionsLogic : ProgressFragmentLogic() {

    val adapter = ComicPagesAdapter(
        listOf(),
        ::onClick
    )
    val clickedComic = MutableLiveData<ComicPage>()

    fun onClick(comic: ComicPage) = clickThrottler.next {
        clickedComic.value = comic
    }

    private val comics: MediatorLiveData<List<Comic>> = MediatorLiveData<List<Comic>>().apply {
        addSource(Source.api.getComics())
    }

    val comicPages: MediatorLiveData<List<ComicPage>> = MediatorLiveData<List<ComicPage>>().apply {
        addSourceNotNull(comics) { list ->
            runAsync {
                Source.apply {
                    api.getUserSubscriptions(userPrefs.uuid).let { subs ->
                        list.mapAsync(bag) { ComicPage(it, Source.api.getUserData(it.authorId) ?: User()) }
                            .filter { it.authorId in subs }
                            .sortedByDescending { it.uploadTimeMs }
                            .also { postValue(it) }
                    }
                }
            }
        }
    }

    fun updateAdapter(list: List<ComicPage>) = adapter.updateData(list)


    fun runAsync(action: suspend () -> Any) {
        AsyncProcessor {
            action()
        } handleError {
            progress.alertAsync(it.message)
        } runWith (bag)
    }
}