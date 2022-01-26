package com.example.traveleisure;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.traveleisure.Model.Model;
import com.example.traveleisure.Model.Destination;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.UUID;


public class EditDestination extends AddPost {

    String destinationId;
    TextView addPost_Title;
    Destination dst;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = super.onCreateView(inflater, container, savedInstanceState);
        addPost_Title= view.findViewById(R.id.addPost_Title);
        destinationId = EditDestinationArgs.fromBundle(getArguments()).getDestinationId();
        Log.d("TAG", "destination id i got: " + destinationId);
        addPost_Title.setText("Edit Post");
        addBtn.setText("Update");
        Model.instance.getDestination(destinationId, new Model.GetDestinationListener() {
            @Override
            public void onComplete(Destination destination) {
                dst=destination;
                destinationNameEditText.setText(destination.getTitleDestination());
                destinationEditText.setText(destination.getDestination());
                avatarImageView.setImageResource(R.drawable.destination_placeholder);
                if(destination.getImageUrl()!=null){
                    Picasso.get().load(destination.getImageUrl()).placeholder(R.drawable.destination_placeholder).into(avatarImageView);
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","Image "+dst.getImageUrl());
                UpdateDestination(v);
            }
        });


        return view;
    }

    private void UpdateDestination(View view){
        if (destinationNameEditText.getText().length() == 0 || destinationEditText.getText().length() == 0 || category.length() == 0) {
            Snackbar.make(view, "You must fill all the fields", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.d("TAG", "Some of the fields are empty.");
        }
        else{
            dst.setTitleDestination(destinationNameEditText.getText().toString());
            dst.setCategory(spinner.getSelectedItem().toString());
            dst.setDestination(destinationEditText.getText().toString());
            dst.getImageUrl();

            addBtn.setEnabled(false);
            cancelBtn.setEnabled((false));
            editImage.setEnabled(false);

            BitmapDrawable drawable = (BitmapDrawable) avatarImageView.getDrawable();
            Bitmap bitmap=drawable.getBitmap();

            Model.instance.uploadImage(drawable.getBitmap(), dst.getId(), new Model.UploadImageListener() {
                @Override
                public void onComplete(String url) {
                    if(url==null){
                        displayFailedError();
                    }
                    else{
                        dst.setImageUrl(url);
                        pb.setVisibility(View.VISIBLE);
                        Model.instance.updateDestination(dst, new Model.AddDestinationListener() {
                            @Override
                            public void onComplete() {
                                pb.setVisibility(View.INVISIBLE);
                                addBtn.setEnabled(true);
                                cancelBtn.setEnabled((false));
                                editImage.setEnabled(false);
                                Navigation.findNavController(addBtn).popBackStack();
                            }
                        });
                    }
                }
            });
        }

    }

}