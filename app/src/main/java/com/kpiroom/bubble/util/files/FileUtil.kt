package com.kpiroom.bubble.util.files

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.kpiroom.bubble.util.constants.FILE_PROVIDER
import java.io.File

fun mkDir(dir: File, subdir: String): File = File(dir, subdir).apply {
    if (!exists())
        mkdirs()
}

fun getFileUri(context: Context, file: File): Uri = FileProvider.getUriForFile(
    context,
    FILE_PROVIDER,
    file
)

fun deleteFile(dir: File, fileName: String) {
    if (fileName.isBlank()) return

    File(dir, fileName).run {
        if (exists())
            delete()
    }
}

fun deleteDir(dir: File) = dir.apply {
    if (isDirectory)
        deleteRecursively()
}

fun getFileExtension(uri: Uri): String = uri.toString().run {
    substring(lastIndexOf("."))
}