package com.example.mymusic.data.repository

import com.example.mymusic.data.room.dao.PlaylistDao
import com.example.mymusic.data.room.entities.Playlist
import com.example.mymusic.data.room.entities.PlaylistSongCrossRef
import com.example.mymusic.data.room.relationship.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class playlistRepository @Inject constructor(
    private val playlistDao: PlaylistDao
) {
    fun insertPlaylist(playlist: Playlist) = playlistDao.insert(playlist)

    fun insertPlaylistSong(playlistSong: PlaylistSongCrossRef) = playlistDao.insertPlaylistSong(playlistSong)

    fun getPlaylists(): Flow<List<PlaylistWithSongs>> = playlistDao.getPlaylists()

    fun getPlaylistById(playlistId: Long): Playlist = playlistDao.getPlaylistById(playlistId)

    fun deletePlaylist(playlist: Playlist) = playlistDao.delete(playlist)

    fun deletePlaylists(playlists: List<Playlist>) = playlistDao.deletePlaylists(playlists)
}