package com.example.mymusic.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mymusic.data.room.entities.MusicPlayedAmount
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicPlayedAmountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: MusicPlayedAmount)

    fun insertWithTimestamp(data: MusicPlayedAmount) {
        insert(data.apply {
            createdAt = System.currentTimeMillis()
            modifiedAt = System.currentTimeMillis()
        })
    }

    @Query("" +
            "UPDATE music_played_Amount " +
            "SET play_count = play_count + 1, modified_at = :modifiedAt " +
            "WHERE song_id = :songId")
    fun songUpdate(songId: Long?, modifiedAt: Long)

    @Query("" +
            "UPDATE music_played_Amount " +
            "SET play_count = play_count + 1, modified_at = :modifiedAt " +
            "WHERE album_id = :albumId")
    fun albumUpdate(albumId: Long?, modifiedAt: Long)

    @Query("" +
            "UPDATE music_played_Amount " +
            "SET play_count = play_count + 1, modified_at = :modifiedAt " +
            "WHERE artist_id = :artistId")
    fun artistUpdate(artistId: Long?, modifiedAt: Long)

    @Query("" +
            "UPDATE music_played_Amount " +
            "SET play_count = play_count + 1, modified_at = :modifiedAt " +
            "WHERE playlist_id = :playlistId")
    fun playlistUpdate(playlistId: Long?, modifiedAt: Long)

    fun updateWithTimestamp(data: MusicPlayedAmount) {
        when {
            data.songId != null -> songUpdate(
                songId = data.songId,
                modifiedAt = System.currentTimeMillis()
            )
            data.albumId != null -> albumUpdate(
                albumId = data.albumId,
                modifiedAt = System.currentTimeMillis()
            )
            data.artistId != null -> artistUpdate(
                artistId = data.artistId,
                modifiedAt = System.currentTimeMillis()
            )
            data.playlistId != null -> playlistUpdate(
                playlistId = data.playlistId,
                modifiedAt = System.currentTimeMillis()
            )
        }
    }

    @Query("SELECT EXISTS(SELECT * FROM music_played_Amount WHERE song_id = :songId)")
    fun songExist(songId: Long): Boolean

    @Query("SELECT EXISTS(SELECT * FROM music_played_Amount WHERE album_id = :albumId)")
    fun albumExist(albumId: Long): Boolean

    @Query("SELECT EXISTS(SELECT * FROM music_played_Amount WHERE artist_id = :artistId)")
    fun artistExist(artistId: Long): Boolean

    @Query("SELECT EXISTS(SELECT * FROM music_played_Amount WHERE playlist_id = :playlistId)")
    fun playlistExist(playlistId: Long): Boolean

    fun exist(data: MusicPlayedAmount): Boolean = when {
        data.songId != null -> songExist(songId = data.songId)
        data.albumId != null -> albumExist(albumId = data.albumId)
        data.artistId != null -> artistExist(artistId = data.artistId)
        data.playlistId != null -> playlistExist(playlistId = data.playlistId)
        else -> false
    }


    @Query("SELECT * FROM music_played_Amount ORDER BY modified_at DESC LIMIT 10")
    fun getRecentlyPlayed(): Flow<List<MusicPlayedAmount>>

    @Query("SELECT * FROM music_played_Amount ORDER BY play_count DESC LIMIT 10")
    fun getMusicPlayedAmounts(): Flow<List<MusicPlayedAmount>>

    @Query("SELECT * FROM music_played_Amount WHERE song_id = :songId")
    fun getMusicPlayedAmountBySongId(songId: Long): Flow<MusicPlayedAmount>

    @Query("SELECT * FROM music_played_Amount WHERE album_id = :albumId")
    fun getMusicPlayedAmountByAlbumId(albumId: Long): Flow<MusicPlayedAmount>

    @Query("SELECT * FROM music_played_Amount WHERE artist_id = :artistId")
    fun getMusicPlayedAmountByArtistId(artistId: Long): Flow<MusicPlayedAmount>

    @Query("SELECT * FROM music_played_Amount WHERE playlist_id = :playlistId")
    fun getMusicPlayedAmountByPlaylistId(playlistId: Long): Flow<MusicPlayedAmount>
}