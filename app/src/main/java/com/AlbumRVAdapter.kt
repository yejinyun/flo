package com

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumBinding

//데이터를 바인딩하기위해 매개변수로 데이터를 받아옴
class AlbumRVAdapter(private val albumList: ArrayList<Album>) : RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>()
{
    //인터페이스에 대한 공부하기
    //클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onItemClick(album: Album)
        fun onRemoveAlbum(position: Int)
    }

    //리스트 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListner: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListner = itemClickListener
    }


    //뷰홀더를 생성해줘야 할 때 호출되는 함수 => 아이템 뷰 객체를 만들어서 뷰홀더에 던져줌
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding:ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    fun addItem(album: Album){
        albumList.add(album)
        //데이터 바뀌었음을 알려줌
        notifyDataSetChanged()
    }

//    fun removeItem(position: Int){
//        //포지션의 데이터를 제거
//        albumList.removeAt(position)
//        notifyDataSetChanged()
//    }


    //뷰홀더에 데이터를 바인딩해줘야 할 때마다 호출되는 함수 => 엄청나게 많이 호출
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])
        //리사이클러뷰에서는 인덱스 아이디를 포지션이라고 부른다.
        holder.itemView.setOnClickListener{
            //아이템뷰의 작업
            mItemClickListner.onItemClick(albumList[position]) }
//        holder.binding.itemAlbumTitleTv.setOnClickListener{ mItemClickListner.onRemoveAlbum(position)}
    }

    //데이터 세트 크기를 알려주는 함수 => 리사이클러뷰 마지막이 언제인지를 알게 된다.
    override fun getItemCount(): Int = albumList.size

    //뷰홀더 > 아이템뷰 객체들을 담는 그릇
    inner class ViewHolder(val binding: ItemAlbumBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(album: Album){
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }
    }
}