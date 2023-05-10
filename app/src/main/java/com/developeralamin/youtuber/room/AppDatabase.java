package com.developeralamin.youtuber.room;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.developeralamin.youtuber.room.table.EntityFavorite;
import com.developeralamin.youtuber.room.table.EntityInfo;
import com.developeralamin.youtuber.room.table.EntityNotification;
import com.developeralamin.youtuber.room.table.EntityWatched;

@Database(
        entities = {EntityFavorite.class, EntityInfo.class, EntityNotification.class, EntityWatched.class},
        version = 1, exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DAO getDAO();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDb(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context, AppDatabase.class, "youtubers_database")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}