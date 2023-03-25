package com.example.wishlist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.rxjava3.annotations.NonNull;

public class Login_Activity extends AppCompatActivity {
    Button btnlog, btnSign;
    EditText mail, pass;

    public static FirebaseAuth firebaseAuth;
    public static String userNameEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnlog = findViewById(R.id.btnLogin);
        btnSign = findViewById(R.id.btnSignIn);
        mail = findViewById(R.id.LoginEmail);
        pass = findViewById(R.id.LoginPassword);
        mail.requestFocus();
        firebaseAuth = FirebaseAuth.getInstance();
        btnSign.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), SignUp.class);
            startActivity(intent);
            finish();
        });
        btnlog.setOnClickListener(v -> {
            if (checkInternet()) {
                ProgressDialog pDialog = new ProgressDialog(Login_Activity.this); //Your Activity.this
                pDialog.setMessage("Please Wait ");
                pDialog.setCancelable(false);
                if ((mail.getText().toString().trim().equals(""))) {
                    mail.requestFocus();
                    mail.setError("Please Enter Email ");
                } else if ((pass.getText().toString().trim().equals(""))) {
                    pass.requestFocus();
                    pass.setError("Please Enter Password ");
                } else {
                    pDialog.show();
                    firebaseAuth.signInWithEmailAndPassword(mail.getText().toString(), pass.getText().toString())
                            .addOnCompleteListener(Login_Activity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        SharedPreferences sharedPreferences = getSharedPreferences("UT", MODE_PRIVATE);
                                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                        myEdit.putBoolean("login", true);
                                        myEdit.putString("email", mail.getText().toString());
                                        myEdit.commit();
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                pDialog.dismiss();

                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                                myEdit.putBoolean("login", true);
                                                myEdit.commit();
                                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                                userNameEmail = firebaseUser.getEmail();
                                                startActivity(intent);
                                            }
                                        }, 1000);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error Email And Password", Toast.LENGTH_SHORT).show();
                                        pDialog.dismiss();
                                    }
                                }
                            });
                }
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

            }

        });
    }

    public boolean checkInternet() {
        boolean state;
        try {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (manager != null) {
                {
                    state = true;
                    networkInfo = manager.getActiveNetworkInfo();
                }
                return networkInfo != null && networkInfo.isConnected();
            } else {
                state = false;
            }
        } catch (NullPointerException e) {
            state = false;
        }
        return state;
    }
}