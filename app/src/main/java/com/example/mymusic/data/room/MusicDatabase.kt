package com.example.mymusic.data.room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.mymusic.data.room.dao.AlbumDao
import com.example.mymusic.data.room.dao.ArtistDao
import com.example.mymusic.data.room.dao.MusicPlayedAmountDao
import com.example.mymusic.data.room.dao.PlaylistDao
import com.example.mymusic.data.room.dao.SongDao
import com.example.mymusic.data.room.entities.Album
import com.example.mymusic.data.room.entities.Artist
import com.example.mymusic.data.room.entities.MusicPlayedAmount
import com.example.mymusic.data.room.entities.Playlist
import com.example.mymusic.data.room.entities.PlaylistSongCrossRef
import com.example.mymusic.data.room.entities.Song
import com.example.mymusic.utils.DATABASE_NAME

@Database(
    entities = [
        Song::class,
        Album::class,
        Artist::class,
        Playlist::class,
        PlaylistSongCrossRef::class,
        MusicPlayedAmount::class
   ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTimeTypeConverters::class)
abstract class MusicDatabase : RoomDatabase() {

    abstract fun artists(): ArtistDao

    abstract fun musicPlayAmount(): MusicPlayedAmountDao

    abstract fun songs(): SongDao

    abstract fun albums(): AlbumDao

    abstract fun playlist(): PlaylistDao

    override fun clearAllTables() {
        TODO("Not yet implemented")
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        TODO("Not yet implemented")
    }

    override fun createOpenHelper(config: DatabaseConfiguration): SupportSQLiteOpenHelper {
        TODO("Not yet implemented")
    }

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: MusicDatabase? = null

        fun getInstance(context: Context): MusicDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): MusicDatabase {
            Log.d("MusicDatabase", "buildDatabase:")
            return Room.databaseBuilder(context, MusicDatabase::class.java, DATABASE_NAME)
//                .addCallback(
//                    object : Callback() {
//                        override fun onCreate(db: SupportSQLiteDatabase) {
//                            super.onCreate(db)
//                            Log.d("MusicDatabase", "buildDatabase: onCreate")
//                            val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
//
//                            WorkManager.getInstance(context).enqueue(request)
//                        }
//                    }
//                )
                .build()
        }

    }
}