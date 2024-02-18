package com.example.mymusic.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mymusic.data.room.entities.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: Song)

    fun insertWithTimestamp(data: Song) {
        insert(data.apply {
            createdAt = System.currentTimeMillis()
            modifiedAt = System.currentTimeMillis()
        })
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllSongs(data: List<Song>)

    @Update
    fun update(data: Song)

    fun updateWithTimestamp(data: Song) {
        update(data.apply {
            modifiedAt = System.currentTimeMillis()
        })
    }

    @Delete
    fun delete(data: Song)

    @Delete
    fun deleteSongs(data: List<Song>)

    @Query("UPDATE songs SET favourite = :isFavourite  WHERE songId = :songId ")
    fun updateFavourite(songId: Long, isFavourite: Boolean)

    @Query("SELECT * FROM songs")
    fun getSongs(): Flow<List<Song>>

    @Query("SELECT COUNT(*) FROM songs")
    fun getSongsCount(): Flow<Int>

    @Query("SELECT * FROM songs WHERE songId = :id")
    fun getSongById(id: Long): Song

    @Query("SELECT * FROM songs WHERE favourite = true")
    fun getFavourites(): List<Song>
}