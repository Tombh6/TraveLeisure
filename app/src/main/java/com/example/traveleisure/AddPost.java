package com.example.traveleisure;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.traveleisure.Model.Model;
import com.example.traveleisure.Model.Destination;
import com.example.traveleisure.Model.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.example.traveleisure.Model.ModelFirebase.getImageFromFireBase;


public class AddPost extends Fragment {


    EditText destinationNameEditText;
    Spinner spinner;//category
    String category;
    EditText destinationEditText;
    ImageView avatarImageView;
    Button editImage;
    Button addBtn;
    Button cancelBtn;
    ProgressBar pb;
    boolean isExist=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_post, container, false);
        avatarImageView = view.findViewById(R.id.addPost_avatar);
        destinationNameEditText= view.findViewById(R.id.addPost_destinationName);
        destinationEditText= view.findViewById(R.id.addPost_Destination);
        String [] categories ={"","Asia","Australia","Africa", "Antarctica","Europe","North America","South America",};
        spinner = (Spinner) view.findViewById(R.id.addPost_Category);
        editImage= view.findViewById(R.id.addPost_uploadPic_btn);
        addBtn = view.findViewById(R.id.addPost_add_btn);
        cancelBtn = view.findViewById(R.id.addPost_cancel_btn);
        pb = view.findViewById(R.id.addPost_progressBar);
        pb.setVisibility(View.INVISIBLE);
        avatarImageView.setVisibility(View.INVISIBLE);

        getImageFromFireBase(User.getInstance().id);
        //category spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                category = spinner.getSelectedItem().toString();
                Log.d("TAG","selected item is: "+category);
            }

            public void onNothingSelected(AdapterView<?> arg) {

            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage();

            }
        });


        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addDestination(v);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });

        return view;
    }



    private void addRecipe(View view){
        if (destinationNameEditText.getText().length() == 0 || destinationEditText.getText().length() == 0 || category.length() == 0) {
            Snackbar.make(view, "You must fill all the fields", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.d("TAG", "Some of the fields are empty.");
        }
        else if(isExist==false){
            Snackbar.make(view, "You must Import a Photo", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.d("TAG", "The Photo didnt Upload");
        }
        else{
            Destination destination= new Destination();
            destination.setId(UUID.randomUUID().toString());
            destination.setTitleDestination(destinationNameEditText.getText().toString());
            destination.setDestination(destinationEditText.getText().toString());
            destination.setCategory(category);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                destination.setUserId(user.getUid());
                destination.setUserName(user.getDisplayName());
            }


            destination.setUserPic(User.getInstance().FBpic);

            addBtn.setEnabled(false);
            cancelBtn.setEnabled((false));
            editImage.setEnabled(false);

            BitmapDrawable drawable = (BitmapDrawable) avatarImageView.getDrawable();
            Bitmap bitmap=drawable.getBitmap();

            Model.instance.uploadImage(drawable.getBitmap(), destination.getId(), new Model.UploadImageListener() {
                @Override
                public void onComplete(String url) {
                    if(url==null){
                        displayFailedError();
                    }
                    else{
                        destination.setImageUrl(url);
                        pb.setVisibility(View.VISIBLE);
                        Model.instance.addDestination(destination, new Model.AddDestinationListener() {
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

    public void displayFailedError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Operation Failed");
        builder.setMessage("Saving image failed, please try again later...");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public void editImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose your destination picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                    isExist=true;
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                    isExist=true;
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        avatarImageView.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                avatarImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }
}