package com.kpiroom.bubble.util.view

import android.view.View
import android.widget.FrameLayout
import android.widget.PopupWindow
import com.kpiroom.bubble.R

class AccountSetupOptionWindow(contentView: View, width: Int, height: Int) : PopupWindow(
    contentView, width, height
) {
    val changePhotoButton: FrameLayout = contentView.findViewById(R.id.setup_change_photo)
    val changeWallpaperButton: FrameLayout = contentView.findViewById(R.id.setup_change_wallpaper)
}