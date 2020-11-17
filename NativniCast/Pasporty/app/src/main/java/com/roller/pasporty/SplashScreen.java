package com.roller.pasporty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    ImageView mainimg;
    TextView welcomeText, descText;
    Button getStarted;
    Animation forimg, frombottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        mainimg = (ImageView) findViewById(R.id.mainimg);
        welcomeText = (TextView) findViewById(R.id.welcomeText);
        descText = (TextView) findViewById(R.id.descText);
        getStarted = (Button) findViewById(R.id.getstarted);

        forimg = AnimationUtils.loadAnimation(this, R.anim.forimg);
        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);

        mainimg.startAnimation(forimg);
        welcomeText.startAnimation(frombottom);
        descText.startAnimation(frombottom);
        getStarted.startAnimation(frombottom);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(i);
                finish();
            }
        });

    }
}
