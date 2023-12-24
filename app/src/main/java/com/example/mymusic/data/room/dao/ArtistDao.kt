package com.example.mymusic.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mymusic.data.room.entities.Artist
import com.example.mymusic.data.room.relationship.ArtistWithSongs
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: Artist)

    fun insertWithTimestamp(data: Artist) {
        insert(data.apply {
            createdAt = System.currentTimeMillis()
            modifiedAt = System.currentTimeMillis()
        })
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllArtists(data: List<Artist>)

    @Update
    fun update(data: Artist)

    fun updateWithTimestamp(data: Artist) {
        update(data.apply {
            modifiedAt = System.currentTimeMillis()
        })
    }

    @Delete
    fun delete(data: Artist)

    @Delete
    fun deleteArtists(data: List<Artist>)

    @Query("SELECT * FROM artist ORDER BY modified_at DESC")
    fun getArtists(): Flow<List<ArtistWithSongs>>

    @Query("SELECT * FROM artist WHERE artistId = :id")
    fun getArtistById(id: Long): Artist


}