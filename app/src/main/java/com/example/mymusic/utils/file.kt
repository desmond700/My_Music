package com.example.mymusic.utils

import android.content.ContentResolver
import android.net.Uri
import com.example.mymusic.MusicPlayerApplication
import java.io.File

fun getMimeType(file: File): String? {
    val context = MusicPlayerApplication.instance
    val uri: Uri = Uri.fromFile(file)
    val resolver: ContentResolver = context.contentResolver

    return resolver.getType(uri)
}