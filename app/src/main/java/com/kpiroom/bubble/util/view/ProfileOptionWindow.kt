package com.kpiroom.bubble.util.view

import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.dichotome.profilebar.R

class ProfileOptionWindow(contentView: View, width: Int, height: Int) : PopupWindow(
    contentView, width, height
) {
    val changePhotoButton: FrameLayout = contentView.findViewById(R.id.change_photo)
    val changeWallpaperButton: FrameLayout = contentView.findViewById(R.id.change_wallpaper)
}