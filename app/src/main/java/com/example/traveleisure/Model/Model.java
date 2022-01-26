package com.example.traveleisure.Model;

import java.util.List;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import com.example.traveleisure.MyApp;

public class Model {
    public static final Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();
    LiveData<List<Destination>> recipeList;

    private Model(){
    }
    public interface Listener<T>{
        void onComplete(T result);
    }
    public interface GetAllRecipesListener{
        void onComplete();
    }

    public LiveData<List<Destination>> getAllRecipes(){
        destinationList = LocalDb.db.destinationDao().getAllDestinations();
        refreshAllDestinations(null);
        return destinationList;
    }

    public LiveData<List<Destination>> getRecipesByCategory(String category) {
        DestinationList = LocalDb.db.destinationDao().getDestinationsByCategory(category);
        refreshAllRecipes(null);
        return destinationList;
    }

    public LiveData<List<Destination>> getAllRecipesPerUser(String userId) {
        destinationList = LocalDb.db.destinationDao().getUserDestination(userId);
        refreshAllDestinations(null);
        return destinationList;
    }

    public void refreshAllDestinations(final GetAllDestinationsListener listener){
        //get local last update date
        final SharedPreferences sp = MyApp.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        long lastUpdated = sp.getLong("lastUpdated", 0);

        //get all updated record from firebase from the last updated
        modelFirebase.getAllDestinations(lastUpdated, new ModelFirebase.GetAllDestinationsListener() {

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<Destination> data) {
                new AsyncTask<String,String,String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        long lastUpdated = 0;
                        for(Destination destination : data){
                            LocalDb.db.recipeDao().insertAll(destination);
                            if (destination.getUpdatedDate() > lastUpdated) lastUpdated = destination.getUpdatedDate();
                        }
                        SharedPreferences.Editor edit = MyApp.context.getSharedPreferences("TAG",Context.MODE_PRIVATE).edit();
                        edit.putLong("DestinationsLastUpdateDate",lastUpdated);
                        edit.commit();
                        return "";
                    }
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        //cleanLocalDb();
                        if (listener!=null)
                            listener.onComplete();
                    }
                }.execute("");
            }
        });
    }

    //Get
    public interface GetDestinationListener{
        void onComplete(Destination destination);
    }
    public void getDestination(String id, GetDestinationListener listener){
        modelFirebase.getDestination(id, listener);
    }

    //Add
    public interface AddDestinationListener{
        void onComplete();
    }
    public void addRecipe(final Destination destination,final AddDestinationListener listener) {
        modelFirebase.addDestination(destination, listener);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                LocalDb.db.destinationDao().insertAll(destination);
                return "";
            }
        }.execute();
    }

    //Update
    public void updateRecipe(final Destination destination,final AddDestinationListener listener) {
        modelFirebase.addDestination(destination, listener);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                LocalDb.db.destinationDao().insertAll(destination);
                return "";
            }
        }.execute();
    }

    //Delete
    public interface DeleteDestinationListener extends AddDestinationListener {}
    public void deleteRecipe(Destination destination, DeleteDestinationListener listener){
        modelFirebase.deleteDestination(destination);
        modelFirebase.delete(destination, listener);
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                LocalDb.db.recipeDao().deleteDestination(destination);
                return "";
            }
        }.execute();
    }

    //Upload
    public interface UploadImageListener extends Listener<String> {}
    public void uploadImage(Bitmap imageBmp, String name, final UploadImageListener listener){
        modelFirebase.uploadImage(imageBmp,name,listener);
    }

    public void updateUserProfile(User user) {
        ModelFirebase.updateUserProfile(user);
    }

}

