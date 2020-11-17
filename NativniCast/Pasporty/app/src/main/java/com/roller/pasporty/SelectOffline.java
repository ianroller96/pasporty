package com.roller.pasporty;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;


public class SelectOffline extends AppCompatActivity {
    Uri vybranySoubor;

    String strPath;
    String extension;

    Button otevritMapu;
    Button souborVyber;

    final RxPermissions rxPermissions = new RxPermissions(this);


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_offline);

        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        //Toast.makeText(SelectOffline.this, "Povolení uděleno!", Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(SelectOffline.this, "Povolení neuděleno!", Toast.LENGTH_SHORT).show();
                    }
                });

        otevritMapu = (Button) findViewById(R.id.otevritMapu);
        souborVyber = (Button) findViewById(R.id.souborVyber);

        souborVyber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectOffline.this);
                builder.setTitle("Upozornění!")
                        .setMessage("Čím větší velikost vybrané vrstvy, tím pomalejší načítání! \n\nDoporučená velikost vrstvy je maximálně 1\u00A0MB!")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent()
                                        .setType("*/*")
                                        .setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Vyberte vrstvu"), 123);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        otevritMapu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vybranySoubor != null && (extension.equals(".geojson") || extension.equals(".json"))) {
                    openOffline();
                } else if (vybranySoubor == null) {
                    Toast.makeText(SelectOffline.this, "Není vybrána žádná vrstva!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SelectOffline.this, "Vybraný soubor není typu GeoJSON!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedfile = data.getData();

            vybranySoubor = selectedfile;
            strPath = FileUtils.getPath(this, vybranySoubor);

            extension = strPath.substring(strPath.lastIndexOf("."));

            Button souborVyber = (Button)findViewById(R.id.souborVyber);
            souborVyber.setText(getFileName(selectedfile));
        }
    }

    public void openOffline() {
        souborVyber = (Button) findViewById(R.id.souborVyber);

        String cesta = "file://" + strPath;

        Intent i = new Intent(this, Offline.class);
        i.putExtra("vybranySoubor", cesta);

        startActivity(i);
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
