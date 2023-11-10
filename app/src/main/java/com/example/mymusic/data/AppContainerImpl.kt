package com.example.mymusic.data

import com.example.mymusic.data.file.FileRepositoryImpl
import com.example.mymusic.data.file.IFileRepository


/**
 * Dependency Injection container at the application level.
 */
interface IAppContainer {
    val fileRepository: IFileRepository
}

class AppContainerImpl: IAppContainer {

    override val fileRepository: IFileRepository by lazy {
        FileRepositoryImpl()
    }
}