package com.kpiroom.bubble.ui.accountSetup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.ActivityAccountSetupBinding
import com.kpiroom.bubble.ui.core.CoreActivity
import com.kpiroom.bubble.ui.login.LoginActivity
import com.kpiroom.bubble.ui.main.MainActivity
import com.kpiroom.bubble.ui.progress.ProgressActivity
import com.kpiroom.bubble.util.constants.dpToPx
import com.kpiroom.bubble.util.imageUpload.createCameraPictureUri
import com.kpiroom.bubble.util.imageUpload.startImageSelectionActivity
import com.kpiroom.bubble.util.livedata.observeTrue
import com.kpiroom.bubble.util.view.AccountSetupOptionWindow
import com.kpiroom.bubble.util.view.hideKeyboard
import kotlinx.android.synthetic.main.activity_account_setup.*

class AccountSetupActivity : ProgressActivity<AccountSetupLogic, ActivityAccountSetupBinding>() {

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

        logic.useCameraForPhoto.observe(this, Observer {
            it?.let { isCamera ->
                usingCamera = isCamera
                addPhoto(isCamera)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (resultCode == Activity.RESULT_OK)
            if (requestCode == REQUEST_PHOTO)
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

    private var optionWindow: AccountSetupOptionWindow? = null
    private fun setupOptionMenu() {
        optionWindow = AccountSetupOptionWindow(
            LayoutInflater.from(this).inflate(
                R.layout.account_setup_popup_window,
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
        photoCaptureUri =
            if (useCamera)
                createCameraPictureUri(this)
            else
                null

        startImageSelectionActivity(this, useCamera, photoCaptureUri)
    }

    override fun onPause() {
        super.onPause()
        optionWindow?.dismiss()
    }

    override fun provideLogic() = ViewModelProviders.of(this).get(AccountSetupLogic::class.java)

    override fun provideLayout() = LayoutBuilder(R.layout.activity_account_setup) {
        logic = this@AccountSetupActivity.logic
    }
}