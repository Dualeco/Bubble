package com.kpiroom.bubble.ui.comicPage

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.R
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.ui.progress.ProgressFragmentLogic
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.constants.DIR_COMICS
import com.kpiroom.bubble.util.constants.STATUS_BAR_SIZE
import com.kpiroom.bubble.util.constants.dpToPx
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.livedata.progressState.alert
import com.kpiroom.bubble.util.livedata.progressState.alertAsync
import com.kpiroom.bubble.util.livedata.progressState.finishAsync
import com.kpiroom.bubble.util.livedata.progressState.loadAsync
import com.kpiroom.bubble.util.livedata.setDefault
import com.kpiroom.bubble.util.recyclerview.model.ComicPage
import java.io.File

class ComicPageLogic : ProgressFragmentLogic() {

    lateinit var comicFile: File
    var comicPage: ComicPage = ComicPage()
        set(value) {
            field = value
            comicFile = File("$DIR_COMICS/${value.id}.pdf")
        }

    val marginTop = STATUS_BAR_SIZE + dpToPx(20)

    val starCount = MutableLiveData<Int>().setDefault(0)
    val starred = MutableLiveData<Boolean>()

    val ownProfileClicked = MutableLiveData<Boolean>()
    val otherAuthorClicked = MutableLiveData<User>()

    val openedPdfUri = MutableLiveData<Uri>()

    fun initStars(): Unit = runAsync {
        Source.apply {
            api.apply {
                starCount.postValue(getStarsForComic(comicPage.id))
                starred.postValue(isComicStarred(userPrefs.uuid, comicPage.id))
            }
        }
    }

    fun onDownloadClicked() = clickThrottler.next {
        if (!comicFile.exists())
            progress.alert(str(R.string.comic_screen_download_comic), ::confirmDownloadAndOpen)
        else
            openComic()
    }


    private fun confirmDownloadAndOpen(confirmed: Boolean) {
        if (confirmed) runAsync {
            progress.loadAsync()
            Source.api.downloadComic(comicPage.id, comicFile)
            progress.finishAsync()

            openComic()
        }
    }

    private fun openComic() {
        openedPdfUri.postValue(comicFile.toUri())
    }

    fun onAuthorClicked() = clickThrottler.next {
        AsyncProcessor {
            if (comicPage.authorId == Source.userPrefs.uuid)
                ownProfileClicked.postValue(true)
            else
                otherAuthorClicked.postValue(Source.api.getUserData(comicPage.authorId))
        } handleError {
            progress.alertAsync(it.message)
        } runWith (bag)
    }

    fun onStarClicked() = clickThrottler.next {
        starred.run {
            value = !(value ?: false).also {
                runAsync {
                    (starCount.value ?: 0).let { count ->
                        Source.apply {
                            if (it) {
                                starCount.postValue(count - 1)
                                api.removeFromFavorites(userPrefs.uuid, comicPage.id)
                            } else {
                                starCount.postValue(count + 1)
                                api.addToFavorites(userPrefs.uuid, comicPage.id)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun runAsync(action: suspend () -> Unit) {
        AsyncProcessor {
            action()
        } handleError {
            progress.alertAsync(it.message)
        } runWith (bag)
    }
}