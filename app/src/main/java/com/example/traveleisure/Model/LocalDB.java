package com.example.traveleisure.Model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.traveleisure.MyApp;


@Database(entities = {Destination.class}, version = 16)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract DestinationDao destinationDao();
}

public class LocalDB{

    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApp.context,
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}