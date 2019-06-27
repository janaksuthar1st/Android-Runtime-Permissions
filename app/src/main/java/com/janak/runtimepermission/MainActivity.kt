package com.janak.runtimepermission

import android.Manifest
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : RunTimePermission(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listeners()
    }

    private fun listeners(){

        btStoragePermission.setOnClickListener(this)
        btLocationPermission.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            btStoragePermission -> {
                requestRuntimePermission(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)) { isPermissionGranted ->
                    if (isPermissionGranted) {

                    }
                }
            }

            btLocationPermission -> {
                requestRuntimePermission(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)) { isPermissionGranted ->
                    if (isPermissionGranted) {

                    }
                }
            }
        }
    }
}
