package com.example.traveleisure.Model;

import android.content.Intent;
import android.widget.EditText;

import androidx.room.Entity;

import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Entity
public class User {

    private static User MyUser = null;


    public String id;
    public String fullName;
    public String email;
    public String password;
    public String profilePic;

    public boolean hasPic=false;
    public String FBpic;
    public String FBname;

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("fullName", fullName);
        result.put("email", email);
        result.put("password", password);
        result.put("profilePic", profilePic);
        return result;
    }

    public User(){
        id = null;
        fullName = null;
        email = null;
        password = null;
        profilePic=null;

        hasPic=false;
        FBname=null;
        FBpic=null;
    }

    public User(String Id,String fullNameInput, String emailInput,String pic) {
        this.id=Id;
        this.fullName = fullNameInput;
        this.email = emailInput;
        this.profilePic=pic;
    }
    public User(String Id,String fullNameInput, String emailInput) {
        this.id=Id;
        this.fullName = fullNameInput;
        this.email = emailInput;
    }

    public static User getInstance() {
        if (MyUser == null)
            MyUser = new User();

        return MyUser;
    }

}
