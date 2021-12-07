package com

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumTable")
data class Album(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0, // album의 pk는 임의로 지정해주기 위해 autogenerate 안씁니다.
    var title:String? = "",
    var singer:String? = "",
    var albuminfo: String? = "",
    var coverImg: Int? = null,
    var isLike: Boolean = false

    //앨범 속의 음악들
//    var songs: ArrayList<String>? = null
)