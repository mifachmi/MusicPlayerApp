package com.fachmi.musicplayerapp.ui.player

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fachmi.musicplayerapp.R
import com.fachmi.musicplayerapp.databinding.ActivityPlaySongBinding
import com.fachmi.musicplayerapp.utils.SongTimer
import com.fachmi.musicplayerapp.utils.SongsManager
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class PlaySongActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener,
    MediaPlayer.OnCompletionListener {

    private lateinit var binding: ActivityPlaySongBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var songManager: SongsManager
    private lateinit var songTimer: SongTimer
    private lateinit var songTitle: String
    var handler = Handler()
    private var seekForwardTime = 5000
    private var seekBackwardTime = 5000
    private var currentSongIndex = 0
    private var isShuffle = false
    private var isRepeat = false
    private var songList = ArrayList<HashMap<String, String>>()
    private var songListSec = HashMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

//        binding.tvJudulLagu.isSelected = true

        //get data intent from adapter
        val intent = intent
//        songListSec = intent.getSerializableExtra("songIndex") as HashMap<String, String>
        val bundle = intent.extras
        if (bundle != null) {
//            Toast.makeText(this, bundle.getValue(bundle.keys.first()), Toast.LENGTH_SHORT).show()
//            Toast.makeText(this, songListSec.getValue(songListSec.keys.first()).toString(), Toast.LENGTH_SHORT).show()
//            Toast.makeText(this, bundle.size, Toast.LENGTH_SHORT).show()
            currentSongIndex = bundle.getInt("songIndex")
//            binding.tvNowPLaying.text = bundle.getValue(bundle.keys.first())
        }

        mediaPlayer = MediaPlayer()
        songManager = SongsManager()
        songTimer = SongTimer()

        binding.seekBar.setOnSeekBarChangeListener(this)
        mediaPlayer.setOnCompletionListener(this)
        songList = songManager.getPlayList()

        //get data song
        getPlaySong(currentSongIndex)

        //methods button action
        getButtonSong()
    }

    private fun getButtonSong() {
        binding.apply {

            imagePlay.setOnClickListener {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                    imagePlay.setBackgroundResource(R.drawable.ic_play)
                } else {
                    mediaPlayer.start()
                    visualizerView.getPathMedia(mediaPlayer)
                    imagePlay.setBackgroundResource(R.drawable.ic_pause)
                }
            }

            imageNext.setOnClickListener {
                currentSongIndex += 1
                if (currentSongIndex < songList.size) {
                    mediaPlayer.stop()
                    imagePlay.setBackgroundResource(R.drawable.ic_play)
                    getPlaySong(currentSongIndex)
                } else {
                    currentSongIndex -= 1
                }
            }

            imagePrev.setOnClickListener {
                currentSongIndex -= 1
                if (currentSongIndex >= 0) {
                    mediaPlayer.stop()
                    imagePlay.setBackgroundResource(R.drawable.ic_play)
                    getPlaySong(currentSongIndex)
                } else {
                    currentSongIndex += 1
                }
            }

            imageForward.setOnClickListener {
                val currentPosition = mediaPlayer.currentPosition
                if (currentPosition + seekForwardTime <= mediaPlayer.duration) {
                    mediaPlayer.seekTo(currentPosition + seekForwardTime)
                } else {
                    mediaPlayer.seekTo(mediaPlayer.duration)
                }
            }

            imageRewind.setOnClickListener {
                val currentPosition = mediaPlayer.currentPosition
                if (currentPosition - seekBackwardTime >= 0) {
                    mediaPlayer.seekTo(currentPosition - seekBackwardTime)
                } else {
                    mediaPlayer.seekTo(0)
                }
            }

            imageRepeat.setOnClickListener {
                if (isRepeat) {
                    isRepeat = false
                    Toast.makeText(this@PlaySongActivity, "Mengulang Lagu", Toast.LENGTH_SHORT)
                        .show()
                    imageRepeat.setImageResource(R.drawable.btn_repeat)
                } else {
                    isRepeat = true
                    Toast.makeText(
                        this@PlaySongActivity,
                        "Mengulang Tidak Aktif",
                        Toast.LENGTH_SHORT
                    ).show()
                    isShuffle = false
                    imageRepeat.setImageResource(R.drawable.btn_repeat_focused)
                    imageShuffle.setImageResource(R.drawable.btn_shuffle)
                }
            }

            imageShuffle.setOnClickListener {
                if (isShuffle) {
                    isShuffle = false
                    Toast.makeText(this@PlaySongActivity, "Acak Lagu, Aktif", Toast.LENGTH_SHORT)
                        .show()
                    imageShuffle.setImageResource(R.drawable.btn_shuffle)
                } else {
                    isShuffle = true
                    Toast.makeText(
                        this@PlaySongActivity,
                        "Acak Lagu, Tidak Aktif",
                        Toast.LENGTH_SHORT
                    ).show()
                    isRepeat = false
                    imageShuffle.setImageResource(R.drawable.btn_shuffle_focused)
                    imageRepeat.setImageResource(R.drawable.btn_repeat)
                }
            }

        }
    }

    private fun getPlaySong(songIndex: Int) {
        try {
            binding.apply {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(songList[songIndex]["songPath"])
                mediaPlayer.prepare()
                songTitle = songList[songIndex]["songTitle"]?.replace("_", " ").toString()
                tvJudulLagu.text = songTitle
                seekBar.progress = 0
                seekBar.max = 100
            }
            // run seekbar
            updateSeekBar()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun updateSeekBar() {
        handler.postDelayed(runnable, 100)
    }

    private val runnable: Runnable by lazy {
        object : Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                val totalDuration = mediaPlayer.duration.toLong()
                val currentDuration = mediaPlayer.currentPosition.toLong()
                binding.tvTotalDuration.text = "" + songTimer.milliSecondsToTimer(totalDuration)
                binding.tvCurrentDuration.text = "" + songTimer.milliSecondsToTimer(currentDuration)
                val progress = songTimer.getProgressPercentage(currentDuration, totalDuration)
                binding.seekBar.progress = progress
                handler.postDelayed(this, 100)
            }
        }
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
        handler.removeCallbacks(runnable)
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        handler.removeCallbacks(runnable)
        val totalDuration = mediaPlayer.duration
        val currentPosition = songTimer.progressToTimer(binding.seekBar.progress, totalDuration)
        mediaPlayer.seekTo(currentPosition)

        //run seekbar
        updateSeekBar()
    }

    override fun onCompletion(p0: MediaPlayer?) {
        if (isRepeat) {
            getPlaySong(currentSongIndex)
        } else if (isShuffle) {
            val rand = Random()
            currentSongIndex = rand.nextInt(songList.size - 1 - 0 + 1) + 0
            getPlaySong(currentSongIndex)
        } else {
            if (currentSongIndex < songList.size - 1) {
                getPlaySong(currentSongIndex + 1)
                currentSongIndex += 1
            } else {
                getPlaySong(0)
                currentSongIndex = 0
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    public override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
    }
}