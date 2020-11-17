package com.roller.pasporty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.Toast;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    CardView online,offline,about,web;

    GridLayout mygrid;
    Animation frombottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        mygrid = (GridLayout) findViewById(R.id.mygrid);

        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);

        mygrid.startAnimation(frombottom);

        // define Cards
        online = (CardView) findViewById(R.id.online);
        offline = (CardView) findViewById(R.id.offline);
        about = (CardView) findViewById(R.id.about);
        web = (CardView) findViewById(R.id.web);

        // add Click listener to the cards
        online.setOnClickListener(this);
        offline.setOnClickListener(this);
        about.setOnClickListener(this);
        web.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.online : i = new Intent(this, SelectOnline.class);
                if (Connectivity.isConnectedFast(this)) {
                    startActivity(i);
                    break;
                }
                else {
                    Toast.makeText(MainMenu.this, "Pomalá rychlost připojení!\n\nNelze otevřít Online Editor!", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.offline : i = new Intent(this, SelectOffline.class); startActivity(i); break;
            case R.id.about : i = new Intent(this, About.class); startActivity(i); break;
            case R.id.web : i = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.mujweb))); startActivity(i); break;
            default : break;
        }
    }
}
