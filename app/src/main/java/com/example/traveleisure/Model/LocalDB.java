package com.example.traveleisure.Model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.traveleisure.MyApp;


@Database(entities = {Destination.class}, version = 16)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract DestinationDao recipeDao();
}

public class LocalDB{

    static public LocalDbRepository db =
            Room.databaseBuilder(MyApp.context,
                    LocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}