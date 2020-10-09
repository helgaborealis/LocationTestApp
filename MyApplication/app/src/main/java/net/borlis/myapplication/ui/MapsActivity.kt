package net.borlis.myapplication.ui

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import net.borlis.myapplication.R
import net.borlis.myapplication.listeners.GoToLocationListener
import net.borlis.myapplication.listeners.PickPhotoListener
import net.borlis.myapplication.sideeffects.SideEffect
import net.borlis.myapplication.sideeffects.SideEffectConsumer
import net.borlis.myapplication.vm.HideKeyBoardSideEffect
import net.borlis.myapplication.vm.MainViewModel
import net.borlis.myapplication.vm.WrongInputSideEffect


@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoToLocationListener,
    SideEffectConsumer, PickPhotoListener {

    private lateinit var mMap: GoogleMap

    private val viewModel: MainViewModel by viewModels()

    override fun onStart() {
        super.onStart()
        viewModel.registerConsumer(this)
    }

    override fun photoPicked(uri: Uri) {
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 42, 42, true)
        viewModel.avatar.value = scaledBitmap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.latlngLiveData.observe(this, {
            mMap.clear()
            mMap.moveCamera(CameraUpdateFactory.newLatLng(it))
            if (viewModel.avatar.value != null) {
                mMap.addMarker(
                    MarkerOptions().position(it).icon(
                        BitmapDescriptorFactory.fromBitmap(
                            viewModel.avatar.value
                        )
                    )
                )
            } else {
                mMap.addMarker(MarkerOptions().position(it))
            }

        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    override fun onGoClick(lat: Editable, lng: Editable) {
        viewModel.onGoClick(lat, lng)
    }

    override fun onStop() {
        super.onStop()
        viewModel.unregisterConsumer(this)
    }

    override fun onSideEffectReceived(sideEffect: SideEffect) {
        when (sideEffect) {
            is WrongInputSideEffect -> Toast.makeText(this, "Wrong input", Toast.LENGTH_SHORT)
                .show()
            is HideKeyBoardSideEffect -> hideKeyboard()
        }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }
}