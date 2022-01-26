package com.example.traveleisure;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.traveleisure.Model.ModelFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText emailInput;
    EditText passwordInput;
    Button loginBtn;
    TextView moveToRegisterBtn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle("Login");

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            ModelFirebase.setUserAppData(firebaseAuth.getCurrentUser().getEmail());
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }

        emailInput = findViewById(R.id.login_email);
        passwordInput = findViewById(R.id.login_password);

        loginBtn = findViewById(R.id.login_login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModelFirebase.loginUser(emailInput.getText().toString(), passwordInput.getText().toString(), new ModelFirebase.Listener<Boolean>() {
                    @Override
                    public void onComplete() {
                        startActivity(new Intent(Login.this, MainActivity.class));
                        finish();
                    }
                    @Override
                    public void onFail() {
                    }
                });
            }
        });

        moveToRegisterBtn = findViewById(R.id.movetoRegister);
        moveToRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

    }
}