package com.example.traveleisure.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DestinationDao {
    @Query("select * from Destination")
    LiveData<List<Destination>> getAllDestinations();

    @Query("select * from Destination where userId = :userId")
    LiveData<List<Destination>> getUserDestinations(String userId);

    @Query("select * from Destination where category= :category")
    LiveData<List<Destination>> getDestinationsByCategory(String category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Destination... destination);

    @Delete
    void deleteDestination(Destination destination);
}
