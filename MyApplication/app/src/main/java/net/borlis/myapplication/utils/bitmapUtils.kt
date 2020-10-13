package net.borlis.myapplication.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun getBitmapAsync(context: Context, uri: Uri): Bitmap? {
    return withContext(Dispatchers.Default) {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        return@withContext Bitmap.createScaledBitmap(bitmap, bitmapScale, bitmapScale, true)
    }
}


private const val bitmapScale = 42