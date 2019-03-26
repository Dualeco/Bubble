package com.kpiroom.bubble.ui.accountSetup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.ActivityAccountSetupBinding
import com.kpiroom.bubble.ui.core.CoreActivity
import com.kpiroom.bubble.ui.login.LoginActivity
import com.kpiroom.bubble.ui.main.MainActivity
import com.kpiroom.bubble.util.constants.FILE_PROVIDER
import com.kpiroom.bubble.util.constants.dpToPx
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.livedata.alert
import com.kpiroom.bubble.util.livedata.observeTrue
import com.kpiroom.bubble.util.view.ProfileOptionWindow
import com.kpiroom.bubble.util.view.hideKeyboard
import kotlinx.android.synthetic.main.activity_account_setup.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AccountSetupActivity : CoreActivity<AccountSetupLogic, ActivityAccountSetupBinding>() {

    private var photoCaptureUri: Uri? = null
    private var usingCamera = false

    companion object {
        const val REQUEST_PHOTO: Int = 0
        fun getIntent(context: Context): Intent = Intent(context, AccountSetupActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupOptionMenu()

        logic.backButtonClicked.observeTrue(this, Observer {
            finish()
            startActivity(LoginActivity.getIntent(this))
            overridePendingTransition(
                R.anim.slide_back_transition,
                R.anim.anim_none
            )
        })

        logic.clearUsernameFocus.observeTrue(this, Observer {
            usernameEditText.clearFocus()
        })

        logic.photoChangeRequested.observeTrue(this, Observer {
            showAlertChoosePhoto()
        })

        logic.wallpaperChangeRequested.observeTrue(this, Observer {
            showAlertChoosePhoto()
        })

        logic.username.observe(this, Observer {
            if (it == "")
                logic.username.value = null
        })

        logic.finishRequested.observeTrue(this, Observer {
            logic.finishSetup()
        })

        logic.setupComplete.observeTrue(this, Observer {
            startActivity(
                MainActivity.getIntent(this)
            )
            finish()
        })

        logic.progress.observe(this, Observer {
            it?.state?.let {
                hideKeyboard()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_PHOTO) {

                logic.apply {
                    val uri = if (usingCamera)
                        photoCaptureUri
                    else
                        intent?.data

                    uri?.let {
                        val isProfilePhoto = photoChangeRequested.value == true
                        dispatchUri(it, isProfilePhoto)
                    }
                }
            }
        }
    }

    private var optionWindow: ProfileOptionWindow? = null
    private fun setupOptionMenu() {
        optionWindow = ProfileOptionWindow(
            LayoutInflater.from(this).inflate(
                R.layout.profile_popup_window,
                account_setup_root,
                false
            ),
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            elevation = dpToPx(8).toFloat()
            isOutsideTouchable = true

            changePhotoButton.setOnClickListener {
                logic.onPhotoChanged()
                dismiss()
            }

            changeWallpaperButton.setOnClickListener {
                logic.onWallpaperChanged()
                dismiss()
            }
        }

        option_button.setOnClickListener {
            optionWindow?.showAsDropDown(popup_anchor)
        }
    }

    private fun addPhoto(useCamera: Boolean) {
        usingCamera = useCamera
        if (useCamera) {
            photoCaptureUri = getFileUri(createTempFile())

            Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, photoCaptureUri)
            }
        } else {
            Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
        }.apply {
            resolveActivity(packageManager)?.let {
                startActivityForResult(this, REQUEST_PHOTO)
            }
        }
    }

    private fun showAlertChoosePhoto() = logic.let {
        it.wallpaperChangeRequested.value.let { wallpaper ->
            it.progress.alert(
                if (wallpaper == true)
                    str(R.string.setup_upload_wallpaper)
                else
                    str(R.string.setup_upload_photo),
                ::addPhoto,
                str(R.string.setup_take_photo_camera),
                str(R.string.setup_from_gallery)
            )
        }
    }

    private fun createTempFile() = File.createTempFile(
        SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()),
        ".jpg",
        getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    )


    private fun getFileUri(file: File) = FileProvider.getUriForFile(
        this,
        FILE_PROVIDER,
        file
    )

    override fun onPause() {
        super.onPause()
        optionWindow?.run {
            dismiss()
        }
    }

    override fun provideLogic() = ViewModelProviders.of(this).get(AccountSetupLogic::class.java)

    override fun provideLayout() = LayoutBuilder(R.layout.activity_account_setup) {
        logic = this@AccountSetupActivity.logic
    }
}