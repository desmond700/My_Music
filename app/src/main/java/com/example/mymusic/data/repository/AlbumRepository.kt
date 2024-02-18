package com.example.mymusic.data.repository

import com.example.mymusic.data.room.dao.AlbumDao
import com.example.mymusic.data.room.entities.Album
import com.example.mymusic.data.room.relationship.AlbumWithSongs
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlbumRepository @Inject constructor(
    private val albumDao: AlbumDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val scope = CoroutineScope(ioDispatcher)

    fun insertAlbum(album: Album) = albumDao.insert(album)

    fun insertAlbumSongs(albums: List<Album>) =
        albumDao.insertAlbumSongs(albums)

    fun insertAllAlbums(albums: List<Album>) {
        albumDao.insertAllAlbums(albums)

//        scope.launch {
//            getAlbums().collectLatest { albumList ->
//                val albumSongsCrossRefs = albumList.map { album ->
//                    albumModels.songs
//                        .filter { it.albumName == album.albumName }
//                        .map { AlbumSongCrossRef(albumId = album.albumId, songId = it.songId) }
//                }.flatten()
//
//                insertAlbumSongs(albumSongsCrossRefs)
//            }
//        }
    }

    fun insertAndDeleteAllAlbums(albums: List<Album>) = albumDao.insertAndDeleteAllAlbums(albums)

    fun getAlbums(): Flow<List<Album>> = albumDao.getAlbums()

    fun getAlbumsCount(): Flow<Int> = albumDao.getAlbumsCount()

    fun getAlbumsWithSongs(): Flow<List<AlbumWithSongs>> = albumDao.getAlbumsWithSongs()

    fun getAlbumWithSongsByAlbumId(albumId: Long) = albumDao.getAlbumsWithSongsByAlbumId(albumId)

    fun getAlbumById(albumId: Long): Album = albumDao.getAlbumById(albumId)

    fun updateAlbum(album: Album) = albumDao.update(album)

    fun deleteAlbum(album: Album) = albumDao.delete(album)

    fun deleteAlbums(albums: List<Album>) = albumDao.deleteAlbums(albums)
}