package com.fachmi.musicplayerapp.ui.dashboard

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.fachmi.musicplayerapp.databinding.ListItemMusicBinding
import com.fachmi.musicplayerapp.ui.player.PlaySongActivity

class MainAdapter(
    private var songList: ArrayList<HashMap<String, String>>,
    private var context: Context
) :
    RecyclerView.Adapter<MainAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ListItemMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var cvListMusic: CardView = binding.cvListMusic
        var textView: TextView = binding.tvJudulLagu
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val viewHolder =
            ListItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = songList[position]["songTitle"]?.replace("_", " ")
        holder.cvListMusic.setOnClickListener {
            val intent = Intent(context, PlaySongActivity::class.java)
//            val indexSong: Int = songList[position]
            intent.putExtra("songIndex", songList[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return songList.size
    }

}