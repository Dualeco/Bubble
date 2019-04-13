package com.kpiroom.bubble.ui.uploadScreen

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.graphics.pdf.PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
import android.net.Uri
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.MutableLiveData
import com.dichotome.profileshared.constants.Constants
import com.kpiroom.bubble.R
import com.kpiroom.bubble.os.BubbleApp.Companion.app
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.Comic
import com.kpiroom.bubble.ui.progress.ProgressFragmentLogic
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.bitmap.getCompressed
import com.kpiroom.bubble.util.constants.STATUS_BAR_SIZE
import com.kpiroom.bubble.util.constants.dpToPx
import com.kpiroom.bubble.util.constants.drw
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.progressState.ProgressState
import com.kpiroom.bubble.util.livedata.progressState.alert
import com.kpiroom.bubble.util.livedata.progressState.alertAsync
import com.kpiroom.bubble.util.livedata.progressState.finishAsync
import com.kpiroom.bubble.util.livedata.progressState.loadAsync
import kotlinx.coroutines.Dispatchers

class UploadScreenLogic : ProgressFragmentLogic() {

    companion object {
        private const val RENDER_MODE = "r"
    }

    val marginTop = STATUS_BAR_SIZE + dpToPx(12)

    val bitmapPreview = MutableLiveData<Bitmap>()
    lateinit var bitmapFavPreview: Bitmap
    lateinit var bitmapThumbnail: Bitmap

    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    private lateinit var uploadUri: Uri

    val uploadClicked = MutableLiveData<Boolean>()
    val uploadComplete = MutableLiveData<Boolean>()

    fun renderPreview(uri: Uri) {
        uploadUri = uri
        progress.apply {
            AsyncProcessor(Dispatchers.IO) {
                loadAsync()
                val bitmap = drw(R.drawable.default_cover).toBitmap().run {
                    Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                }
                PdfRenderer(app.contentResolver.openFileDescriptor(uri, RENDER_MODE))
                    .openPage(0).apply {
                        render(bitmap, null, null, RENDER_MODE_FOR_DISPLAY)
                        close()
                    }
                bitmapFavPreview = bitmap.run {
                    Bitmap.createScaledBitmap(this, width / 3, height / 3, false)
                }
                bitmapThumbnail = bitmap.run {
                    Bitmap.createScaledBitmap(this, width / 8, height / 8, false)
                }
                bitmapPreview.postValue(bitmap)
                finishAsync()
            } handleError {
                alertAsync(it.message)
            } runWith (bag)
        }
    }

    fun onUpload() {
        uploadClicked.value = true

        val title = title.value
        val description = description.value

        val preview = bitmapPreview.value ?: return

        if (title.isNullOrBlank() || description.isNullOrBlank())
            progress.alert(str(R.string.upload_screen_empty_fields))
        else
            Source.apply {
                val uploadTimeMs = System.currentTimeMillis()
                val comicId = "${userPrefs.uuid}-$uploadTimeMs"

                progress.apply {
                    AsyncProcessor {
                        loadAsync()
                        api.apply {
                            uploadComic(comicId, uploadUri)
                            uploadComicData(
                                comicId,
                                Comic(
                                    comicId,
                                    title,
                                    uploadComicThumbnail(comicId, bitmapThumbnail).toString(),
                                    uploadComicFavPreview(comicId, bitmapFavPreview).toString(),
                                    uploadComicPreview(comicId, preview).toString(),
                                    description,
                                    userPrefs.uuid,
                                    uploadTimeMs
                                )
                            )
                        }
                        finishAsync()
                        uploadComplete.postValue(true)
                    } handleError {
                        alertAsync(it.message)
                    } runWith (bag)
                }
            }
    }
}