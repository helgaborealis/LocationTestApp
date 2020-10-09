package net.borlis.myapplication.listeners

import android.net.Uri

interface PickPhotoListener {
    fun photoPicked(uri: Uri)
}