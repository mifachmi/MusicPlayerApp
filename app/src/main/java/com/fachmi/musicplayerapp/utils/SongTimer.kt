package com.fachmi.musicplayerapp.utils

class SongTimer {
    fun milliSecondsToTimer(milliseconds: Long): String {
        var finalTimerString = ""
        var secondsString = ""
        val hours = (milliseconds / (1000 * 60 * 60)).toInt()
        val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
        val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
        if (hours > 0) {
            finalTimerString = "$hours:"
        }
        secondsString = if (seconds < 10) {
            "0$seconds"
        } else {
            "" + seconds
        }
        finalTimerString = "$finalTimerString$minutes:$secondsString"
        return finalTimerString
    }

    fun getProgressPercentage(currentDuration: Long, totalDuration: Long): Int {
        val percentage: Double
        val currentSeconds: Long = (currentDuration / 1000)
        val totalSeconds: Long = (totalDuration / 1000)
        percentage = currentSeconds.toDouble() / totalSeconds * 100
        return percentage.toInt()
    }

    fun progressToTimer(progress: Int, _totalDuration: Int): Int {
        var totalDuration = _totalDuration
        var currentDuration = 0
        totalDuration = (totalDuration / 1000)
        currentDuration = (progress.toDouble() / 100 * totalDuration).toInt()
        return currentDuration * 1000
    }

}