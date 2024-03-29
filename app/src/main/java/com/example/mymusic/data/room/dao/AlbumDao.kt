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
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: Album)

    fun insertWithTimestamp(data: Album) {
        insert(data.apply {
            createdAt = System.currentTimeMillis()
            modifiedAt = System.currentTimeMillis()
        })
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllAlbums(data: List<Album>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAlbumSongs(albumSongCrossRef: List<Album>)

    @Transaction
    fun insertAndDeleteAllAlbums(data: List<Album>) {
        deleteAllEntries()
        insertAllAlbums(data)
    }

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

    @Query("DELETE FROM albums")
    fun deleteAllEntries()

    @Query("SELECT * FROM albums ORDER BY modified_at DESC")
    fun getAlbums(): Flow<List<Album>>

    @Query("SELECT COUNT(*) FROM albums")
    fun getAlbumsCount(): Flow<Int>

    @Query("SELECT * FROM albums WHERE albumId = :albumId")
    fun getAlbumsWithSongsByAlbumId(albumId: Long): Flow<AlbumWithSongs>

    @Transaction
    @Query("SELECT * FROM albums ORDER BY modified_at DESC")
    fun getAlbumsWithSongs(): Flow<List<AlbumWithSongs>>

    @Query("SELECT * FROM albums WHERE albumId = :id")
    fun getAlbumById(id: Long): Album

}
