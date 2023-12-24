package com.example.mymusic.data.room.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.example.mymusic.data.room.entities.Album
import com.example.mymusic.data.room.entities.Song

class AlbumWithSongs(
    @Embedded
    val album: Album,

    @Relation(
        parentColumn = "album_name",
        entityColumn = "album_name"
    )
    val songs: List<Song>
)