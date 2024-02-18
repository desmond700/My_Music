package com.example.mymusic.data.repository

import com.example.mymusic.data.room.dao.PlaylistDao
import com.example.mymusic.data.room.entities.Playlist
import com.example.mymusic.data.room.entities.PlaylistSongCrossRef
import com.example.mymusic.data.room.relationship.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlaylistRepository @Inject constructor(
    private val playlistDao: PlaylistDao
) {
    fun insertPlaylist(playlist: Playlist) = playlistDao.insert(playlist)

    fun insertPlaylistSong(playlistSong: PlaylistSongCrossRef) = playlistDao.insertPlaylistSong(playlistSong)

    fun insertPlaylistSongs(playlistSongs: List<PlaylistSongCrossRef>) = playlistDao.insertPlaylistSongs(playlistSongs)

    fun getPlaylists(): Flow<List<PlaylistWithSongs>> = playlistDao.getPlaylists()

    fun getPlaylistsCount(): Flow<Int> = playlistDao.getPlaylistsCount()

    fun getPlaylistById(playlistId: Long) = playlistDao.getPlaylistById(playlistId)

    fun getPlaylistWithSongsByPlaylistId(playlistId: Long) = playlistDao.getPlaylistsByPlaylistId(playlistId)

    fun updatePlaylist(playlist: Playlist) = playlistDao.update(playlist)

    fun deletePlaylist(playlist: Playlist) = playlistDao.delete(playlist)

    fun deletePlaylists(playlists: List<Playlist>) = playlistDao.deletePlaylists(playlists)
}