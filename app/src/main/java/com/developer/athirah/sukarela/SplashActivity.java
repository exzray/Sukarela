package com.developer.athirah.sukarela;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class SplashActivity extends AppCompatActivity {

    // declare view
    private CircleImageView image;
    private LinearLayout container1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // init view
        image = findViewById(R.id.splash_image);
        container1 = findViewById(R.id.splash_container1);

        initUI();
    }

    private void initUI() {

        // wait to timeout before go to main activity
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

        // get animation xml use for this activity
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.anim_appear);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.anim_shake);

        // play the animation
        image.startAnimation(animation2);
        container1.startAnimation(animation1);
    }
}
