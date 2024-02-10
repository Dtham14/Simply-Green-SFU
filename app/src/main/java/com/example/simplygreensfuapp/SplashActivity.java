package com.example.simplygreensfuapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static int sfu_screen = 7000;
    private static boolean paused = false;
    //variables
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView logo, slogan;
    private Handler handler = new Handler();
    private Runnable runnable = this::SkipSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        //Animations

        topAnim = AnimationUtils.loadAnimation(this, R.anim.topanimation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottomanimation);
        //hooks

        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView1);
        slogan = findViewById(R.id.textView2);

        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

        handler.postDelayed(runnable
                , sfu_screen);

    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {
            SkipSplash();
        }
    }

    //function called to finish this splash activity
    public void SkipSplash() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void SkipSplash(View view) {
        SkipSplash();
    }

}