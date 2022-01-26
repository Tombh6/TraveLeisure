package com.example.traveleisure;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.traveleisure.Model.Model;
import com.example.traveleisure.Model.Destination;

import java.util.List;

public class DestinationViewModel extends ViewModel {

    private LiveData<List<Destination>> recipeLiveData;

    public DestinationViewModel() {
        Log.d("TAG", "RecipeViewModel");
        destinationLiveData = Model.instance.getAllDestinations();
    }

    public LiveData<List<Destination>> getDestinationsByCategory(String categoryId){
        destinationLiveData = Model.instance.getDestinationsByCategory(categoryId);
        return DestinationLiveData;
    }

    public LiveData<List<Destination>> getDataByUser(String userId){
        LiveData<List<Destination>> myRecipeLiveData;
        myDestinationLiveData = Model.instance.getAllDestinationsPerUser(userId);
        return myDestinationLiveData;
    }

    public LiveData<List<Destination>> getData() {
        return destinationLiveData;
    }

}
