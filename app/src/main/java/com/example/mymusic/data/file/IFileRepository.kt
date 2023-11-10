package com.example.mymusic.data.file

import com.example.mymusic.data.Result
import com.example.mymusic.model.MusicFile
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IFileRepository {
    /**
     * Get the files from whatever the directory provided
     * params file - Represents a directory
     * rerturns List<file> - Represents a list of music files
     */
    suspend fun getFiles(file: File): Result<List<MusicFile>>

    /**
     * Observe the current favourites.
     */
    fun observeFavourites(): Flow<Set<String>>

    /**
     * Toggle a fileUri to be a favourite or not.
     */
    suspend fun toggleFavourite(fileUri: String)
}