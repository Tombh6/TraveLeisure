package com.example.traveleisure.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.example.traveleisure.AddPost;
import com.example.traveleisure.MyApplication;
import com.example.traveleisure.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class ModelFirebase {
//TBH's user
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    public static int destinationsCounter=0;

    public void deleteDestination(Destination destination) {
        db.collection("Deleted Destinations")
                .document(destination.getId()).set(destination.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "-Destination remove Successfully-");
            }

        });
    }

    public interface GetAllDestinationsListener{
        void onComplete(List<Destination> list);
    }

    public void addDestination(Destination destination, final Model.AddDestinationListener listener) {
        db.collection("destinations")
                .document(destination.getId()).set(destination.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "-Destination added Successfully-");
                listener.onComplete();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TAG", "-Error adding destination-", e);
                listener.onComplete();
            }
        });
    }


    public void getAllDestinations(Long lastUpdated, final GetAllDestinationsListener listener) {
        Timestamp ts = new Timestamp(lastUpdated, 0);
        db.collection("destinations").whereGreaterThan("lastUpdated", ts)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Destination> destinationList = new LinkedList<Destination>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Destination dst = new Destination();
                                dst.fromMap(document.getData());
                                destinationList.add(dst);
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                        listener.onComplete(destinationList);

                    }
                });
    }

    public void getDestination(String id, final Model.GetDestinationListener listener) {
        db.collection("destinations").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Destination destination = null;
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc!=null) {
                        destination = new Destination();
                        destination.fromMap(task.getResult().getData());
                    }
                }
                listener.onComplete(destination);
            }
        });
    }

    public void delete(Destination destination, Model.DeleteDestinationListener listener) {
        db.collection("destinations").document(destination.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onComplete();
            }
        });
    }

    public interface Listener<T>{
        void onComplete();
        void onFail();
    }

    public static void registerUserAccount(final String fullName, String password, final String email,final String profilePic, final Listener<Boolean> listener) {
        if (firebaseAuth.getCurrentUser() != null){
            firebaseAuth.signOut();
        }
        if (firebaseAuth.getCurrentUser() == null &&
                fullName != null && !fullName.equals("") &&
                password != null && !password.equals("") &&
                email != null && !email.equals("")){
            Map<String,Object> data = new HashMap<>();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(MyApplication.context, "User registered", Toast.LENGTH_SHORT).show();

                    // Map<String,Object> data = new HashMap<>();
                    data.put("id",FirebaseAuth.getInstance().getCurrentUser().getUid());
                    data.put("profilePic",null);
                    data.put("fullName", fullName);
                    data.put("email", email);
                    data.put("password", password);
                    db.collection("userProfileData").document(email).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "User has created in userProfileData Collection");
                            User user = new User(null,fullName,email,profilePic);
                            setUserAppData(email);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MyApplication.context, "Fails to create user and upload data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    // CreateUserProfile(email,fullName,password,profilePic);
                    listener.onComplete();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MyApplication.context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    listener.onFail();
                }
            });
        }
    }

    public static void uploadUserData(final String fullName, final String email){

        //data.put("profileImageUrl", task.getResult().toString());
        Map<String,Object> data = new HashMap<>();
        data.put("fullName", fullName);
        data.put("email", email);
        //data.put("info", "NA");
        db.collection("userProfileData").document(email).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MyApplication.context, "User has created in userProfileData Collection", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MyApplication.context, "Fails to create user and upload data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static  void updateUserProfile(User user){

        if(user.profilePic==null){
            db.collection("userProfileData").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            if(document.getData().get("id").equals(user.id)){
                                if(document.getData().get("profilePic")!=null){
                                    String url= (String) document.getData().get("profilePic");
                                    user.profilePic=url;
                                }
                                db.collection("userProfileData")
                                        .document(User.getInstance().email).set(user.toMap());
                            }
                        }
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }


                }
            });

        }else{
            db.collection("userProfileData")
                    .document(User.getInstance().email).set(user.toMap());
        }
        db.collection("destination").whereEqualTo("userId",user.id).get().addOnCompleteListener(new  OnCompleteListener<QuerySnapshot>(){

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Destination dst= new Destination();
                        dst.setCategory(doc.getData().get("category").toString());
                        dst.setId(doc.getData().get("id").toString());
                        dst.setImageUrl(doc.getData().get("imageUrl").toString());
                        dst.setDestination(doc.getData().get("destination").toString());
                        dst.setTitleDestination(doc.getData().get("titleDestination").toString());
                        dst.setUserId(doc.getData().get("userId").toString());

                        dst.setUserName(user.fullName);
                        if( user.profilePic!=null){
                            dst.setUserPic(user.profilePic);
                        }
                        Log.d("update","destination Data: "+dst.getUserPic());
                        Log.d("update","destination Data: "+dst.getUserName());

                        db.collection("destinations")
                                .document(dst.getId()).set(dst.toMap());
                    }
                }
            }
        });

    }

    public static void CreateUserProfile(String email,String fullName, String password, String profilePic) {
        Map<String, Object> data = new HashMap<>();
        if (email != null)
            data.put("email",email);
        if (fullName != null)
            data.put("fullName", fullName);
        if (profilePic != null)
            data.put("profilePic", profilePic);
        if (password != null)
            data.put("password", password);
        Log.d("TAG","email: "+email);
        Log.d("TAG","fullName: "+fullName);
        Log.d("TAG","profilePic: "+profilePic);
        Log.d("TAG","password: "+password);

        db.collection("userProfileData").document(User.getInstance().email).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MyApplication.context, "-Profile Updates Successfully- " , Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static void loginUser(final String email, String password, final Listener<Boolean> listener){
        Log.d("TAG", "LOGIN");
        if (email != null && !email.equals("") && password != null && !password.equals("")){
            if (firebaseAuth.getCurrentUser() != null) {
                firebaseAuth.signOut();
            }
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(MyApplication.context, "Login Succeeded!", Toast.LENGTH_SHORT).show();
                    setUserAppData(email);
                    listener.onComplete();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MyApplication.context, "Failed to login: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    listener.onFail();
                }
            });
        }
        else {
            Toast.makeText(MyApplication.context, "Please fill both data fields", Toast.LENGTH_SHORT).show();
        }

    }


    public static void setUserAppData(String email) {
        db.collection("userProfileData").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    User.getInstance().profilePic=(String) task.getResult().get("profilePic");
                    User.getInstance().fullName = (String) task.getResult().get("fullName");
                    User.getInstance().password = (String) task.getResult().get("password");
                    User.getInstance().email = email;
                    User.getInstance().id = firebaseAuth.getUid();
                }
            }
        });
    }

    public static void signOut(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
    }

    public static void getImageFromFireBase(String userId){

        db.collection("userProfileData").whereEqualTo("id",userId).get().addOnCompleteListener((OnCompleteListener<QuerySnapshot>) task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d("TAG", "test" + document.getId() + " => " + document.getData().get("profilePic"));
                    User.getInstance().FBpic=(String) document.getData().get("profilePic");
                }
            } else {
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });
    }



    public void uploadImage(Bitmap imageBmp, String name,  Model.UploadImageListener listener){
        final StorageReference imagesRef = storage.getReference().child("images").child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        listener.onComplete(downloadUrl.toString());
                    }
                });
            }
        });
    }



}
