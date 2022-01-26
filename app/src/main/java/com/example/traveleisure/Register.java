package com.example.traveleisure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.traveleisure.Model.ModelFirebase;
import com.example.traveleisure.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class Register extends AppCompatActivity {

    EditText fullNameInput;
    EditText passwordInput;
    EditText emailInput;
    Button registerBtn;
    TextView moveToLoginBtn;
    String profilePic=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.setTitle("Sign Up");

        fullNameInput = findViewById(R.id.register_fullName);
        passwordInput = findViewById(R.id.register_password);
        emailInput = findViewById(R.id.register_email);
        registerBtn = findViewById(R.id.register_register_btn);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelFirebase.registerUserAccount(fullNameInput.getText().toString(), passwordInput.getText().toString(),
                        emailInput.getText().toString(), profilePic,new ModelFirebase.Listener<Boolean>() {
                            @Override
                            public void onComplete() {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(fullNameInput.getText().toString())
                                        .build();
                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            startActivity(new Intent(Register.this, MainActivity.class));
                                            finish();
                                        }
                                    }
                                });
                            }
                            @Override
                            public void onFail() {
                            }
                        });
            }
        });



        moveToLoginBtn = findViewById(R.id.register_login_btn);
        moveToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
    }
}

