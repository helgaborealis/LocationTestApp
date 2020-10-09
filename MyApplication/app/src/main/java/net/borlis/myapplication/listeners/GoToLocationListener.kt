package net.borlis.myapplication.listeners

import android.text.Editable

interface GoToLocationListener {
    fun onGoClick(lat:Editable, lng:Editable)
}