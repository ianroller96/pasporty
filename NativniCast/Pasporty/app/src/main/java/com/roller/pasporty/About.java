package com.roller.pasporty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element Mail = new Element();
        Mail.setTitle("E-mail");
        Mail.setIconDrawable(R.drawable.about_icon_email);
        Mail.setIntent(new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","ianroller96@gmail.com", null)));

        Element Web = new Element();
        Web.setTitle("Webové stránky");
        Web.setIconDrawable(R.drawable.about_icon_link);
        Web.setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.mujweb))));

        Element LinkedIn = new Element();
        LinkedIn.setTitle("LinkedIn");
        LinkedIn.setIconDrawable(R.drawable.about_icon_linkedin);
        LinkedIn.setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/ianroller96/")));

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.ic_launcher)
                .setDescription(getString(R.string.popis_about))
                .addGroup("O aplikaci")
                .addItem(new Element().setTitle("\u00A9 Jan ROLLER, 2019–2020"))
                .addItem(new Element().setTitle("Katedra geoinformatiky PřF UPOL"))
                .addGroup("Použité knihovny")
                .addItem(new Element().setTitle("osmdroid, osmbonuspack, lottie, android-about-page, RxPermissions; Leaflet, jQuery; Leaflet.EasyButton, leaflet-locatecontrol, leaflet-search; Connectivity.java"))
                .addGroup("Použité mapové zdroje")
                .addItem(new Element().setTitle("\u00A9 Seznam.cz a.s., \u00A9 OpenStreetMap, \u00A9 ČÚZK; \u00A9 OpenTopoMap, \u00A9 USGS"))
                .addGroup("Kontakty")
                .addItem(Web)
                .addItem(Mail)
                .addItem(LinkedIn)
                .create();

        setContentView(aboutPage);
    }
}