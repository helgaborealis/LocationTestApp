package net.borlis.myapplication.ui

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_user.*
import net.borlis.myapplication.R
import net.borlis.myapplication.listeners.GoToLocationListener
import net.borlis.myapplication.listeners.PickPhotoListener

@AndroidEntryPoint
class UserFragment() : Fragment() {
    private lateinit var goToLocationListener: GoToLocationListener
    private lateinit var pickPhotoListenr: PickPhotoListener


    override fun onAttach(context: Context) {
        super.onAttach(context)
        goToLocationListener = context as GoToLocationListener
        pickPhotoListenr = context as PickPhotoListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_user, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        set_photo_button.setOnClickListener { requestPhotoFromExternalStorage() }
        go_button.setOnClickListener {
            goToLocationListener.onGoClick(
                lat_edittext.text,
                lng_edittext.text
            )
        }
    }

    private fun requestPhotoFromExternalStorage() {
        activity?.let {
            if (ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                ActivityCompat
                    .requestPermissions(
                        it,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        READ_EXTERNAL_STORAGE_REQUEST_CODE
                    );
            } else {
                pickPhotoFromExternalStorage()
            }

        }

    }

    private fun pickPhotoFromExternalStorage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_PHOTO_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                pickPhotoFromExternalStorage()
            } else {
                Toast.makeText(
                    activity,
                    "Camera Permission Denied",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    private fun setUserAvatar(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .into(imageView);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICK_PHOTO_REQUEST_CODE -> {
                if (resultCode == RESULT_OK && data != null && data.data != null) {
                    val uri = data.data
                    uri?.let {
                        setUserAvatar(uri)
                        pickPhotoListenr.photoPicked(uri)
                    }
                }
            }
        }
    }

    companion object {
        const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 877
        const val PICK_PHOTO_REQUEST_CODE = 129
    }

}