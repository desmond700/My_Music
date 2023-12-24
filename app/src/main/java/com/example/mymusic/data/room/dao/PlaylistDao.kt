package com.example.mymusic.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mymusic.data.room.entities.Playlist
import com.example.mymusic.data.room.entities.PlaylistSongCrossRef
import com.example.mymusic.data.room.relationship.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: Playlist)

    fun insertWithTimestamp(data: Playlist) {
        insert(data.apply {
            createdAt = System.currentTimeMillis()
            modifiedAt = System.currentTimeMillis()
        })
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPlaylistSong(data: PlaylistSongCrossRef)

    @Update
    fun update(data: Playlist)

    @Delete
    fun deletePlaylists(data: List<Playlist>)

    fun updateWithTimestamp(data: Playlist) {
        update(data.apply {
            modifiedAt = System.currentTimeMillis()
        })
    }

    @Delete
    fun delete(data: Playlist)

    @Transaction
    @Query("SELECT * FROM playlists ORDER BY modified_at DESC")
    fun getPlaylists(): Flow<List<PlaylistWithSongs>>

    @Query("SELECT * FROM playlists WHERE playlistId = :id")
    fun getPlaylistById(id: Long): Playlist

}
