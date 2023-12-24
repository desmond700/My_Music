package com.example.mymusic.data.repository

import com.example.mymusic.data.room.dao.ArtistDao
import com.example.mymusic.data.room.entities.Artist
import com.example.mymusic.data.room.relationship.ArtistWithSongs
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArtistRepository @Inject constructor(
    private val artistDao: ArtistDao
) {
    fun insertArtist(artist: Artist) = artistDao.insert(artist)

    fun insertAllArtists(artists: List<Artist>) = artistDao.insertAllArtists(artists)

    fun getArtists(): Flow<List<ArtistWithSongs>> = artistDao.getArtists()

    fun getArtistById(artistId: Long): Artist = artistDao.getArtistById(artistId)

    fun deleteArtist(artist: Artist) = artistDao.delete(artist)

    fun deleteArtists(artists: List<Artist>) = artistDao.deleteArtists(artists)
}