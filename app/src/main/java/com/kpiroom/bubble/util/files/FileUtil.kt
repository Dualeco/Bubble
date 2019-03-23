package com.kpiroom.bubble.util.files

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

fun mkDir(dir: File, subdir: String): File = File(dir, subdir).apply {
    if (!exists())
        mkdirs()
}

fun getFileUri(context: Context, file: File): Uri = FileProvider.getUriForFile(
    context,
    "com.kpiroom.bubble.fileprovider",
    file
)

fun createImageFile(dir: File, name: String): File = File(dir, "$name.jpg")

fun createImageInSubdir(dir: File, subdir: String, fileName: String): File = createImageFile(
    mkDir(dir, subdir),
    fileName
)