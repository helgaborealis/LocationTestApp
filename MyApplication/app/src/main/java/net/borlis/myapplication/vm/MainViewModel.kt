package net.borlis.myapplication.vm

import android.graphics.Bitmap
import android.net.Uri
import android.text.Editable
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import net.borlis.myapplication.base.BaseViewModel
import net.borlis.myapplication.domain.CoordinatesUiModel

class MainViewModel @ViewModelInject constructor() : BaseViewModel<CoordinatesUiModel>() {

    var latlngLiveData: MutableLiveData<LatLng> = MutableLiveData()
    var avatar:MutableLiveData<Bitmap> = MutableLiveData()

    fun onGoClick(lat: Editable, lng: Editable) {

        HideKeyBoardSideEffect.post()
        if (lat.isNotEmpty() && lng.isNotEmpty() && correctCoordinates(
                lat.toString().toDouble(),
                lng.toString().toDouble()
            )
        ) {
            latlngLiveData.value = LatLng(lat.toString().toDouble(), lng.toString().toDouble())
        } else {
            WrongInputSideEffect.post()
        }
        {

        }
    }

    private fun correctCoordinates(lat: Double, lng: Double) =
        lat in (-90.0..90.0) && lng in (-180.0..180.0)


}