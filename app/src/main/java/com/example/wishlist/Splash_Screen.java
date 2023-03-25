package com.example.wishlist;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

public class Splash_Screen extends AppCompatActivity {

    private static int time =2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sh = getSharedPreferences("UT",MODE_PRIVATE);
        boolean log = sh.getBoolean("login", false);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            Intent intent;
            @Override
            public void run() {
                 if (log==true){
                     intent =new Intent(Splash_Screen.this,
                            MainActivity.class);
                }
                else {
                    intent =new Intent(Splash_Screen.this,
                            Login_Activity.class);
                }
                startActivity(intent);

                finish();
            }
        }, time);
    }
}