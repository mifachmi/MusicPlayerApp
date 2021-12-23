package com.fachmi.musicplayerapp.utils

import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FilenameFilter
import java.util.*

class SongsManager {

    private var mediaPath =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
    private val songsList = ArrayList<HashMap<String, String>>()

    fun getPlayList(): ArrayList<HashMap<String, String>> {
        val home = File(mediaPath)
        Log.d("direktori: ", home.toString())

        if (home.listFiles(FileExtensionFilter()) != null) {
            for (file in home.listFiles(FileExtensionFilter())) {
                val song = HashMap<String, String>()
                song["songTitle"] = file.name.substring(0, file.name.length - 4)
                song["songPath"] = file.path
                songsList.add(song)
            }
        }
        return songsList
    }

    internal inner class FileExtensionFilter : FilenameFilter {
        override fun accept(dir: File, name: String): Boolean {
            return name.endsWith(".mp3") || name.endsWith(".MP3")
        }
    }

}