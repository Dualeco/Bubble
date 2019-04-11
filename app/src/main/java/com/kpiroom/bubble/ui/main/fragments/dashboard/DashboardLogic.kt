package com.kpiroom.bubble.ui.main.fragments.dashboard

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.ui.progress.ProgressFragmentLogic
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.collectionsAsync.mapAsync
import com.kpiroom.bubble.util.progressState.ProgressState
import com.kpiroom.bubble.util.livedata.progressState.alertAsync
import com.kpiroom.bubble.util.livedata.progressState.finishAsync
import com.kpiroom.bubble.util.livedata.progressState.load
import com.kpiroom.bubble.util.recyclerview.AllComicsAdapter
import com.kpiroom.bubble.util.recyclerview.model.NamedComic

class DashboardLogic : ProgressFragmentLogic() {
    override val progress = MutableLiveData<ProgressState>().apply {
        load()
    }

    val adapter = AllComicsAdapter(
        listOf(),
        ::onClick
    )

    fun onClick(comic: NamedComic) {

    }

    fun onRem(uuid: String) {

    }

    private val comics = Source.api.getAllUploadsLiveData()

    val namedComics = MediatorLiveData<List<NamedComic>>().apply {
        addSource(comics) { res ->
            res.data?.let { list ->
                progress.apply {
                    AsyncProcessor {
                        list.mapAsync(bag) {
                            NamedComic(
                                it.uuid,
                                it.title,
                                it.thumbnailUrl,
                                it.description,
                                Source.api.getUsername(it.authorId) ?: "",
                                it.uploadTimeMs,
                                it.downloads,
                                it.stars
                            )
                        }.sortedByDescending {
                            it.uploadTimeMs
                        }.also { postValue(it) }
                        finishAsync()
                    } handleError {
                        progress.alertAsync(it.message)
                    } runWith (bag)
                }
            }
        }
    }

    fun updateAdapter(list: List<NamedComic>) = adapter.updateData(list)
}