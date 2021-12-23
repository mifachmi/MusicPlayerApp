package com.fachmi.musicplayerapp.ui.dashboard

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.fachmi.musicplayerapp.databinding.ActivityMainBinding
import com.fachmi.musicplayerapp.utils.SongsManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var songsAdapter: MainAdapter
    private lateinit var songsManager: SongsManager

    private var songList = ArrayList<HashMap<String, String>>()

    private var permissionArrays = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO,
        Manifest.permission.MODIFY_AUDIO_SETTINGS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val setPermission = Build.VERSION.SDK_INT

        if (setPermission > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (checkIfAlreadyhavePermission()
                && checkIfAlreadyhavePermission2()
                && checkIfAlreadyhavePermission3()
                && checkIfAlreadyhavePermission4()
            ) {
                Log.d("TAG", "permission aman")
            } else {
                requestPermissions(permissionArrays, 101)
            }
        }

        //transparent background searchview
        val searchPlateId =
            binding.svLagu.context.resources.getIdentifier(
                "android:id/search_plate",
                null,
                null
            )
        val searchPlate = binding.svLagu.findViewById<View>(searchPlateId)
        searchPlate?.setBackgroundColor(Color.TRANSPARENT)

        songsManager = SongsManager()
        songList = songsManager.getPlayList()
        songsAdapter = MainAdapter(songList, this)
        binding.apply {
            rvListMusic.setHasFixedSize(true)
            rvListMusic.layoutManager = LinearLayoutManager(applicationContext)
            rvListMusic.adapter = songsAdapter
        }
    }

    private fun checkIfAlreadyhavePermission(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun checkIfAlreadyhavePermission2(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun checkIfAlreadyhavePermission3(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun checkIfAlreadyhavePermission4(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS)
        return result == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                val intent = intent
                finish()
                startActivity(intent)
            }
        }
    }

//    companion object {
//        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
//            val window = activity.window
//            val layoutParams = window.attributes
//            if (on) {
//                layoutParams.flags = layoutParams.flags or bits
//            } else {
//                layoutParams.flags = layoutParams.flags and bits.inv()
//            }
//            window.attributes = layoutParams
//        }
//    }
}