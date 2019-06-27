package com.janak.runtimepermission

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import android.text.Html
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("Registered")
open class RunTimePermission : AppCompatActivity() {

    private var callback: ((Boolean) -> Unit)? = null

    fun requestRuntimePermission(permissions: Array<String>, callback: (isPermissionGranted: Boolean) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var granted = true
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    granted = false
                    break
                }
            }


            if (granted) {
                callback(true)
            } else {
                this.callback = callback
                requestPermissions(permissions, Integer.MAX_VALUE)
            }
        } else callback(true)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Integer.MAX_VALUE) {
            var granted = true
            for (i in 0 until grantResults.size) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    granted = false
                    break
                }
            }
            if (granted)
                callback?.invoke(true)
            else onPermissionDenied()
        }
    }

    private fun onPermissionDenied() {
        callback?.invoke(false)
        showAlertDialog()
    }

    private fun showAlertDialog() {
        val adb = AlertDialog.Builder(this)
        adb.setTitle(getString(R.string.app_name))
        val msg = "<p>Dear User, </p>" +
                "<p>It Seems like you have Denied the minimum requirement permission to access more features of this application.</p>" +
                "<p>You must have to Allow all the permission.</p>" +
                "<p>Do you want to enable all the requirement permission ?</p>" +
                "<p>Go To : Settings >> App > " + getString(R.string.app_name) + " Permission : Allow All</p>"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            adb.setMessage(Html.fromHtml(msg, Html.FROM_HTML_MODE_LEGACY))
        else
            adb.setMessage(Html.fromHtml(msg))
        adb.setPositiveButton("Allow All") { dialog, _ ->
            dialog.dismiss()
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }

        adb.setNegativeButton("Remind Me Later") { dialog, _ -> dialog.dismiss() }
        if (!isFinishing)
            adb.show()
    }
}