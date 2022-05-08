package com.example.shop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {


    private static final String LOG_TAG = LoginActivity.class.getName();
    private static final String PREF_KEY = LoginActivity.class.getPackage().toString();
    private static final int RC_SIGN_IN = 123;
    private static final int SECRET_KEY = 99;

    EditText emailET;
    EditText passwordET;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailET = findViewById(R.id.editTextEmail);
        passwordET = findViewById(R.id.editTextPassword);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();


        Log.i(LOG_TAG, "onCreate");
    }


    public void login(View view) {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();


        if (email.length() == 0) {
            Toast.makeText(LoginActivity.this, "You have to type in your email.", Toast.LENGTH_LONG).show();
        } else if (password.length() == 0) {
            Toast.makeText(LoginActivity.this, "You have to type in password.", Toast.LENGTH_LONG).show();
        } else {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(LOG_TAG, "Login done!");
                        startShopping();
                    } else {


                        Toast.makeText(LoginActivity.this, "Invalid email or password!", Toast.LENGTH_LONG).show();
                    }



                }
            });
        }
    }

    private void startShopping() {
        Intent intent = new Intent(this, ShopListActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }


    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}




