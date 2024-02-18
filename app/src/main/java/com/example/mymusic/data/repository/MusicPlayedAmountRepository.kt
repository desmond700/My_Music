package com.example.mymusic.data.repository

import android.util.Log
import com.example.mymusic.data.room.dao.AlbumDao
import com.example.mymusic.data.room.dao.ArtistDao
import com.example.mymusic.data.room.dao.MusicPlayedAmountDao
import com.example.mymusic.data.room.dao.SongDao
import com.example.mymusic.data.room.entities.MusicPlayedAmount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class MusicPlayedAmountRepository @Inject constructor(
    private val musicPlayedAmountDao: MusicPlayedAmountDao,
    private val albumDao: AlbumDao,
    private val artistDao: ArtistDao,
    private val songDao: SongDao
) {
    fun insert(data: MusicPlayedAmount) = musicPlayedAmountDao.insertWithTimestamp(data)

    fun update(data: MusicPlayedAmount) {
        val exist = musicPlayedAmountDao.exist(data)
        Log.d(TAG, "update data $data")
        Log.d(TAG, "update exist $exist")
        when(exist) {
            true -> musicPlayedAmountDao.updateWithTimestamp(data)
            else -> musicPlayedAmountDao.insertWithTimestamp(data)
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getRecentlyPlayed() = musicPlayedAmountDao.getRecentlyPlayed()
        .flatMapLatest { recents ->
            channelFlow {
                Log.d(TAG, "getRecentlyPlayed recents $recents")

                val recentlyPlayedEntities = recents.mapNotNull {
                    getEntity(
                        albumId = it.albumId,
                        artistId = it.artistId,
                        songId = it.songId
                    )
                }

                send(recentlyPlayedEntities)
            }
        }


    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getMostPlayed(): Flow<List<Any>> = musicPlayedAmountDao.getMusicPlayedAmounts()
        .flatMapLatest { amounts ->
            channelFlow {
                val mostPlayedEntities = amounts.mapNotNull {
                    getEntity(
                        albumId = it.albumId,
                        artistId = it.artistId,
                        songId = it.songId
                    )
                }

                send(mostPlayedEntities)
            }
        }

    private fun getEntity(albumId: Long?, artistId: Long?, songId: Long?): Any? = when {
        albumId != null -> albumDao.getAlbumById(id = albumId)
        artistId != null -> artistDao.getArtistById(id = artistId)
        songId != null -> songDao.getSongById(id = songId)
        else -> null
    }

    companion object {
        const val TAG = "MusicPlayedAmountRepository"
    }
}