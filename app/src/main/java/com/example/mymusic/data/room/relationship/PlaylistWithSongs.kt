package com.example.mymusic.data.room.relationship

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.mymusic.data.room.entities.Playlist
import com.example.mymusic.data.room.entities.PlaylistSongCrossRef
import com.example.mymusic.data.room.entities.Song

data class PlaylistWithSongs(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "songId",
        associateBy = Junction(PlaylistSongCrossRef::class)
    )
    val songs: List<Song>
)