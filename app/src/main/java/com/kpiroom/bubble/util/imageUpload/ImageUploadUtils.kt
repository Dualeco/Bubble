package com.kpiroom.bubble.util.imageUpload

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.R
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.ui.accountSetup.AccountSetupActivity
import com.kpiroom.bubble.util.constants.DIR_CAMERA
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.date.timeStamp
import com.kpiroom.bubble.util.files.*
import com.kpiroom.bubble.util.progressState.ProgressState
import com.kpiroom.bubble.util.livedata.progressState.alert
import java.io.File


fun showImageSelectionAlert(
    progress: MutableLiveData<ProgressState>,
    isWallpaper: Boolean,
    callback: (Boolean) -> Unit
) {
    progress.alert(
        if (isWallpaper)
            str(R.string.setup_upload_wallpaper)
        else
            str(R.string.setup_upload_photo),
        callback,
        str(R.string.setup_take_photo_camera),
        str(R.string.setup_from_gallery)
    )
}

fun startImageSelectionActivity(activity: FragmentActivity, useCamera: Boolean, imageFromPhotoUri: Uri?) {
    activity.apply {
        val intent = if (useCamera)
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, imageFromPhotoUri)
            }
        else
            Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )

        intent.apply {
            resolveActivity(packageManager)?.let {
                startActivityForResult(this, AccountSetupActivity.REQUEST_PHOTO)
            }
        }
    }
}

fun startImageSelectionActivity(fragment: Fragment, useCamera: Boolean, imageFromPhotoUri: Uri?) {
    fragment.apply {
        val intent = if (useCamera)
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, imageFromPhotoUri)
            }
        else
            Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )

        intent.apply {
            activity?.let {
                resolveActivity(it.packageManager)?.let {
                    startActivityForResult(this, AccountSetupActivity.REQUEST_PHOTO)
                }
            }
        }
    }
}

fun createCameraPictureUri(context: Context): Uri? = getFileUri(
    context,
    File(DIR_CAMERA, timeStamp)
)