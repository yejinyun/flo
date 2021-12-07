package com

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.databinding.ItemSavedsongBinding

class SongRVAdapter (val context: Context) :
    RecyclerView.Adapter<SongRVAdapter.ViewHolder>() {
    private val songs = ArrayList<Song>()

    interface MyItemClickListener{
        fun onRemoveSavedsong(position: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    //뷰홀더 생성
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding:ItemSavedsongBinding = ItemSavedsongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position])
        holder.binding.songMoreIv.setOnClickListener {
            mItemClickListener.onRemoveSavedsong(songs[position].id)
            onRemoveSong(position)
        }
    }

    override fun getItemCount(): Int = songs.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    fun onRemoveSong(position: Int){
        songs.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemSavedsongBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(song: Song){
            if(song.coverImgUrl ==""){
                Glide.with(context).load(song.coverImg!!).into(binding.itemSavesongAlbumIv)
            } else {
                Glide.with(context).load(song.coverImgUrl).into(binding.itemSavesongAlbumIv)
            }
            binding.itemSavesongTitleTv.text = song.title
            binding.itemSavesongSingerTv.text = song.singer
        }
    }
}