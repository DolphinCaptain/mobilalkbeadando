package com.example.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = RegisterActivity.class.getPackage().toString();
    EditText userNameET;
    EditText emailET;
    EditText pwET;
    EditText pwConET;
    EditText phoneET;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    private FirebaseFirestore mFirestore;
    private CollectionReference mProfiles;

    Button c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 99) {
            finish();
        }

//
       // c=(Button)findViewById(R.id.cancel";
//
//
       // c.setOnClickListener(new View.OnClickListener() {
       //     @Override
       //     public void onClick(View view) {
       //         Animation animation= AnimationUtils.loadAnimation(RegisterActivity.this,R.anim.fadein);
       //         c.startAnimation(animation);
       //     }
       // });
       //
        userNameET = findViewById(R.id.userNameEditText);
        emailET = findViewById(R.id.userEmailEditText);
        pwET = findViewById(R.id.passwordEditText);
        pwConET = findViewById(R.id.passwordAgainEditText);
        phoneET = findViewById(R.id.phoneEditText);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        mFirestore = FirebaseFirestore.getInstance();
        mProfiles = mFirestore.collection("Profiles");

        String username = preferences.getString("userName", "");
        String password = preferences.getString("password", "");

        userNameET.setText(username);
        pwET.setText(password);

        mAuth = FirebaseAuth.getInstance();
    }


    public void register(View view) {
        String username = userNameET.getText().toString();
        String email = emailET.getText().toString();
        String pw = pwET.getText().toString();
        String pwConfirm = pwConET.getText().toString();
        String phone = phoneET.getText().toString();

        if (username.length() == 0) {
            Toast.makeText(RegisterActivity.this, "You have to write a name.", Toast.LENGTH_LONG).show();
        } else if (email.length() == 0) {
            Toast.makeText(RegisterActivity.this, "You have to write an email.", Toast.LENGTH_LONG).show();
        } else if (pw.length() == 0) {
            Toast.makeText(RegisterActivity.this, "You have to write a password.", Toast.LENGTH_LONG).show();
        } else if (!pw.equals(pwConfirm)) {
            Toast.makeText(RegisterActivity.this, "The password and the confirm password are not the same.", Toast.LENGTH_LONG).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mProfiles.add(new Profile(username, email, phone));
                        Log.d(LOG_TAG, "User created successfully");
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("email", emailET.getText().toString());
                        editor.putString("password", pwET.getText().toString());
                        editor.apply();
                        finish();

                    } else {
                        Log.d(LOG_TAG, "User hasn't created successfully");
                        Toast.makeText(RegisterActivity.this, "User hasn't created successfully " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


    public void cancel(View view) {
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}