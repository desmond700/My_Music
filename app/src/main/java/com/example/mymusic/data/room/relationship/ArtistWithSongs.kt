package com.example.mymusic.data.room.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.example.mymusic.data.room.entities.Artist
import com.example.mymusic.data.room.entities.Song

class ArtistWithSongs(
    @Embedded
    val album: Artist,

    @Relation(
        parentColumn = "artist_name",
        entityColumn = "artist_name"
    )
    val songs: List<Song>
)