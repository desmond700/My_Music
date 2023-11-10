package com.example.mymusic.data.file

import android.util.Log
import com.example.mymusic.data.Result
import com.example.mymusic.model.MusicFile
import com.example.mymusic.utils.getMimeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File

class FileRepositoryImpl: IFileRepository {
    private val favourites = MutableStateFlow<Set<String>>(setOf())

    override suspend fun getFiles(file: File): Result<List<MusicFile>> {
        val musicFiles = mutableListOf<MusicFile>()

        // All files present inside the parent and child directories
        val allFiles = file.listFiles()

        if (allFiles.isNullOrEmpty()) {
            return Result.Error(IllegalArgumentException("No files found"))
        }

        // Loop through all the files to map the native #File to out #MusicFile object
        allFiles.forEach { mediaFile ->
            // If file is a sub-directory, then dive inside that too
            // Respected the privacy, we don't want to show/read the hidden files
            mediaFile.apply {
                if (isDirectory && !isHidden) {
                    getFiles(this)
                }
                else {
                    val mimeType = getMimeType(mediaFile)
                    Log.d("FileRepositoryImpl", "getFiles file mimeType $mimeType")
                    Log.d("FileRepositoryImpl", "getFiles file extension ${this.extension}")
                    if (listOf("mp3").contains(this.extension)) {
                        // If you want to filter particular types of files like the pdf|txt|jpg, then
                        // with the following check, you can check the file extension or multiple kinds
                        // of extensions
                        // if (name.endsWith(".ext") {
                        // }
                        musicFiles.add(
                            MusicFile(
                                name = nameWithoutExtension,
                                modifiedAt = lastModified(),
                                size = length(),
                                path = absolutePath
                            )
                        )
                    }

                }
            }
        }

        return Result.Success(musicFiles)
    }

    override fun observeFavourites(): Flow<Set<String>> = favourites

    override suspend fun toggleFavourite(fileUri: String) {
        val set = favourites.value.toMutableSet()

        if (!set.add(fileUri)) {
            set.remove(fileUri)
        }

        favourites.value = set.toSet()
    }
}