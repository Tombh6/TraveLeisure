package com.example.traveleisure;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.traveleisure.Model.Model;
import com.example.traveleisure.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class EditProfile extends Fragment {

    CircleImageView profilePic;
    EditText newFullName;
    View view;
    FirebaseUser user;

    boolean isExist=false;
    User currentUser;
    CircleImageView p;
    UserProfileChangeRequest profileUpdates;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();

        profilePic=view.findViewById(R.id.detailsprofile_profile_im);
        if (User.getInstance().profilePic != null) {
            Picasso.get().load(User.getInstance().profilePic).noPlaceholder().into(profilePic);
        }

        newFullName = view.findViewById(R.id.editTextTextPersonName);
        newFullName.setText(user.getDisplayName());

        Button updateProfilePic=view.findViewById(R.id.editProfile_upload_Btn);
        Button save = view.findViewById(R.id.editProfile_Save_Btn);
        Button cancel = view.findViewById(R.id.editProfile_cencel_Btn);

        p=view.findViewById(R.id.profile_profile_im);



        updateProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImage();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });
        return view;
    }


    private void updateUserProfile() {

        if (isExist){
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newFullName.getText().toString())
                    .build();
            BitmapDrawable drawable = (BitmapDrawable) profilePic.getDrawable();
            Model.instance.uploadImage(/*bitmapSelectedImage*/drawable.getBitmap(), user.getEmail(), new Model.UploadImageListener() {
                @Override
                public void onComplete(String url) {
                    if(url==null){
                        displayFailedError();
                    }
                    else{
                        currentUser= new User( user.getUid(),newFullName.getText().toString(),user.getEmail(),url);
                        Model.instance.updateUserProfile(currentUser);

                    }

                }
            });

        }
        else{
            currentUser= new User( user.getUid(),newFullName.getText().toString(),user.getEmail());
            Model.instance.updateUserProfile(currentUser);
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newFullName.getText().toString())
                    .build();

        }
        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    NavController navCtrl = Navigation.findNavController(view);
                    navCtrl.popBackStack();
                    navCtrl.popBackStack();
                }
            }
        });

    }


    private void editImage() {
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
                        Bitmap bitmapSelectedImage = (Bitmap) data.getExtras().get("data");
                        profilePic.setImageBitmap(bitmapSelectedImage);
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
                                profilePic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
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
}