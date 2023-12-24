package com.example.mymusic.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mymusic.data.room.entities.Album
import com.example.mymusic.data.room.relationship.AlbumWithSongs
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: Album)

    fun insertWithTimestamp(data: Album) {
        insert(data.apply {
            createdAt = System.currentTimeMillis()
            modifiedAt = System.currentTimeMillis()
        })
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAlbums(data: List<Album>)

    @Update
    fun update(data: Album)

    fun updateWithTimestamp(data: Album) {
        update(data.apply {
            modifiedAt = System.currentTimeMillis()
        })
    }

    @Delete
    fun delete(data: Album)

    @Delete
    fun deleteAlbums(data: List<Album>)

    @Transaction
    @Query("SELECT * FROM albums ORDER BY modified_at DESC")
    fun getAlbums(): Flow<List<AlbumWithSongs>>

    @Query("SELECT * FROM albums WHERE id = :id")
    fun getAlbumById(id: Long): Album

}
