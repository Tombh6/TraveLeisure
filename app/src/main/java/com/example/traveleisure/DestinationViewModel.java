package com.example.traveleisure;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.traveleisure.Model.Model;
import com.example.traveleisure.Model.Destination;

import java.util.List;

public class DestinationViewModel extends ViewModel {

    private LiveData<List<Destination>> destinationLiveData;

    public DestinationViewModel() {
        Log.d("TAG", "DestinationViewModel");
        destinationLiveData = Model.instance.getAllDestinations();
    }

    public LiveData<List<Destination>> getDestinationsByCategory(String categoryId){
        destinationLiveData = Model.instance.getDestinationsByCategory(categoryId);
        return destinationLiveData;
    }

    public LiveData<List<Destination>> getDataByUser(String userId){
        LiveData<List<Destination>> myDestinationLiveData;
        myDestinationLiveData = Model.instance.getAllDestinationsPerUser(userId);
        return myDestinationLiveData;
    }

    public LiveData<List<Destination>> getData() {
        return destinationLiveData;
    }

}
