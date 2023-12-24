package com.example.mymusic.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mymusic.data.room.entities.MusicPlayedAmount

@Dao
interface MusicPlayedAmountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: MusicPlayedAmount)

    @Update
    fun update(data: MusicPlayedAmount)

    fun updateWithTimestamp(data: MusicPlayedAmount) {
        update(data.apply {
            modifiedAt = System.currentTimeMillis()
        })
    }

    @Query("SELECT * FROM music_played_Amount ORDER BY play_count")
    fun getMusicPlayedAmounts(): List<MusicPlayedAmount>

    @Query("SELECT * FROM music_played_Amount WHERE songId = :songId")
    fun getMusicPlayedAmountBySongId(songId: Long): MusicPlayedAmount
}