package com.example.mymusic.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.mymusic.data.room.entities.PlaylistSongCrossRef

@Dao
interface PlaylistWithSongsCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: PlaylistSongCrossRef)

    @Update
    fun update(data: PlaylistSongCrossRef)

    @Delete
    fun delete(data: PlaylistSongCrossRef)
}