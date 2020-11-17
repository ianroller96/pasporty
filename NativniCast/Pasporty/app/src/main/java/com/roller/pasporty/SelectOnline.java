package com.roller.pasporty;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SelectOnline extends AppCompatActivity {

    public static final String EXTRA_TEXT = "com.roller.pasporty.EXTRA_TEXT";
    private static final int PERMISSION_STORAGE_CODE = 1000;

    final RxPermissions rxPermissions = new RxPermissions(this);

    Button otevritMapu;
    Spinner spinnerVyber;
    ImageView stahnoutVrstvu;

    public boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_online);

        otevritMapu = (Button) findViewById(R.id.otevritMapu);
        spinnerVyber = (Spinner) findViewById(R.id.spinnerVyber);
        stahnoutVrstvu = (ImageView) findViewById(R.id.stahnoutVrstvu);

        checkIfOnline();

        otevritMapu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOnline();
            }
        });

        if (connected) {
            addItemsOnSpinner();
        }
        else {
            otevritMapu.setAlpha(.5f);
            otevritMapu.setClickable(false);

            spinnerVyber.setAlpha(.5f);
            spinnerVyber.setClickable(false);

            stahnoutVrstvu.setAlpha(.5f);
            stahnoutVrstvu.setClickable(false);
        }
    }

    public void addItemsOnSpinner() {
        spinnerVyber = (Spinner) findViewById(R.id.spinnerVyber);

        String soubory = null;
        String[] pole_souboru;
        List<String> filenames = new ArrayList<String>();

        /*FETCH DATA*/
        fetchData d = new fetchData();
        d.execute(getString(R.string.web) + "vypis.php");
        try {
            soubory = d.get();
        } catch (Exception e) {
            Log.e("ERROR IN THREAD", e.toString());
        }

        /*ROZSEKAT STRING*/
        pole_souboru = soubory.split(";");

        /*PRESUNOUT STRING DO "List<string> filenames" KROME PRVNIHO, KTEREJ JE NULL*/
        for (int i = 1; i < pole_souboru.length; i++) {
            filenames.add(pole_souboru[i]);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                this,
                R.layout.custom_spinner,
                filenames);
        dataAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        Collections.sort(filenames);
        spinnerVyber.setAdapter(dataAdapter);
    }

    public void checkIfOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //jsme pripojeni k siti
            connected = true;
            Toast.makeText(SelectOnline.this,
                    "Připojeno k serveru!", Toast.LENGTH_SHORT).show();
        }
        else {
            connected = false;
            Toast.makeText(SelectOnline.this,
                    "Chyba připojení!", Toast.LENGTH_LONG).show();
        }
    }

    public void openOnline() {
        spinnerVyber = (Spinner) findViewById(R.id.spinnerVyber);
        String soubor = spinnerVyber.getSelectedItem().toString();

        Intent intent = new Intent(this, Online.class);
        intent.putExtra(EXTRA_TEXT, soubor);
        startActivity(intent);
    }

    public void showAlertDialog(View v) {
        String soubor = spinnerVyber.getSelectedItem().toString();

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Stáhnout vrstvu?");
            alert.setMessage("Chcete do zařízení stáhnout vrstvu " + soubor + "?");
            alert.setPositiveButton("Stáhnout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startDownloading();
                    Toast.makeText(SelectOnline.this, "Vrstva se stahuje...", Toast.LENGTH_SHORT).show();
                }
            });
            alert.setNegativeButton("Zrušit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            alert.create().show();
    }

    @SuppressLint("CheckResult")
    private void startDownloading() {
        String soubor = spinnerVyber.getSelectedItem().toString();

        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(getString(R.string.web) + "data/" + soubor);

                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setTitle(soubor);
                        request.setDescription("Vrstva se stahuje...");
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setVisibleInDownloadsUi(false);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, soubor);

                        downloadmanager.enqueue(request);
                    } else {
                        Toast.makeText(SelectOnline.this, "Povolení zakázáno!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

