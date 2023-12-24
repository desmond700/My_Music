package com.example.mymusic.data.repository

import com.example.mymusic.data.room.dao.AlbumDao
import com.example.mymusic.data.room.entities.Album
import com.example.mymusic.data.room.relationship.AlbumWithSongs
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlbumRepository @Inject constructor(
    private val albumDao: AlbumDao
) {
    fun insertAlbum(album: Album) = albumDao.insert(album)

    fun insertAllAlbums(albums: List<Album>) = albumDao.insertAllAlbums(albums)

    fun getAlbums(): Flow<List<AlbumWithSongs>> = albumDao.getAlbums()

    fun getAlbumById(albumId: Long): Album = albumDao.getAlbumById(albumId)

    fun deleteAlbum(album: Album) = albumDao.delete(album)

    fun deleteAlbums(albums: List<Album>) = albumDao.deleteAlbums(albums)
}