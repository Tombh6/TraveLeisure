package com.example.traveleisure.Model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.traveleisure.MyApplication;



@Database(entities = {Destination.class}, version = 1)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract DestinationDao destinationDao();
}

public class AppLocalDb{

    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.context,
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}