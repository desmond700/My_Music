package com.example.mymusic.data.repository

import com.example.mymusic.data.room.dao.SongDao
import com.example.mymusic.data.room.entities.Song
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SongRepository @Inject constructor(
    private val songDao: SongDao
) {
    fun insertSong(song: Song) = songDao.insert(song)

    fun insertAllSongs(songs: List<Song>) = songDao.insertAllSongs(songs)

    fun getFavourites(): List<Song> = songDao.getFavourites()

    fun getAllSongs(): Flow<List<Song>> = songDao.getSongs()

    fun getAllSongCount() : Flow<Int> = songDao.getSongsCount()

    fun getSongById(songId: Long): Song = songDao.getSongById(songId)

    fun updateSong(song: Song) = songDao.update(song)

    fun deleteSong(song: Song) = songDao.delete(song)

    fun deleteSongs(songs: List<Song>) = songDao.deleteSongs(songs)

    fun updateFavourite(songId: Long, isFavourite: Boolean) = songDao.updateFavourite(songId, isFavourite)
}