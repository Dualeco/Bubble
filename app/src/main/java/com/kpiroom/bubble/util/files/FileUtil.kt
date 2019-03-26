package com.kpiroom.bubble.util.files

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun mkDir(dir: File, subdir: String): File = File(dir, subdir).apply {
    if (!exists())
        mkdirs()
}

fun getFileUri(context: Context, file: File): Uri = FileProvider.getUriForFile(
    context,
    "com.kpiroom.bubble.fileprovider",
    file
)

fun deleteFile(dir: File, fileName: String) =
    File(dir, fileName).run {
        if (exists())
            delete()
    }

fun deleteDir(dir: File) = dir.apply {
    if (isDirectory)
        deleteRecursively()
}

fun getFileExtension(uri: Uri): String = uri.toString().run {
    substring(lastIndexOf("."))
}