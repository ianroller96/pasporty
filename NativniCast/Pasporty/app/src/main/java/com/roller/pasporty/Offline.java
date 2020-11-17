package com.roller.pasporty;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.Style;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Offline extends AppCompatActivity{
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;

    Uri souborUri;
    File soubor = null;

    boolean booLokace = false;
    ITileSource tileSource;

    FloatingActionButton fab_centermap;
    FloatingActionButton fab_location;

    @SuppressLint("CheckResult")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's
        //tile servers will get you banned based on this string

        //inflate and create the map
        setContentView(R.layout.activity_offline);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        /*PERMISSIONS PRO JISTOTU*/
        requestPermissionsIfNecessary(new String[] {
                // if you need to show the current location, uncomment the line below
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });

        /*OSM BONUS PACK*/
        IMapController mapController = map.getController();

        /*LOADING GeoJSON*/
        KmlDocument kmlDocument = new KmlDocument();

        Bundle extras = getIntent().getExtras();
        souborUri = Uri.parse(extras.getString("vybranySoubor"));
        soubor = new File(souborUri.getPath());
        kmlDocument.parseGeoJSON(soubor);

        /*ZOOM NA VRSTVU*/
        BoundingBox bb = kmlDocument.mKmlRoot.getBoundingBox();
        mapController.setCenter(bb.getCenterWithDateLine());
        mapController.setZoom(15.0);
        map.invalidate();

        /*STYL*/
        Drawable defaultMarker = getResources().getDrawable(R.drawable.ic_marker);
        Bitmap defaultBitmap = ((BitmapDrawable)defaultMarker).getBitmap();
        Style defaultStyle = new Style(defaultBitmap, 0xFFEB6D25, 6.0f, 0x80EB6D25);
        FolderOverlay kmlOverlay = (FolderOverlay)kmlDocument.mKmlRoot.buildOverlay(map, defaultStyle, null, kmlDocument);
        map.getOverlays().add(kmlOverlay);
        map.invalidate();

        /*CONTROLS*/
        map.setMultiTouchControls(true);

        /*TLACITKA*/
        fab_centermap = (FloatingActionButton) findViewById(R.id.fab_centermap);
        fab_location = (FloatingActionButton) findViewById(R.id.fab_location);

        //Center Map
        fab_centermap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoundingBox bb = kmlDocument.mKmlRoot.getBoundingBox();
                mapController.animateTo(bb.getCenterWithDateLine());
                mapController.setZoom(15.0);
                map.invalidate();
            }
        });

        //Lokace
        GpsMyLocationProvider prov = new GpsMyLocationProvider(Offline.this);
        prov.addLocationSource(LocationManager.NETWORK_PROVIDER);
        MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(prov, map);

        fab_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (booLokace == false) {
                    locationOverlay.enableMyLocation();
                    map.getOverlayManager().add(locationOverlay);
                    mapController.animateTo(locationOverlay.getMyLocation());
                    booLokace = true;
                } else {
                    locationOverlay.disableMyLocation();
                    map.getOverlayManager().remove(locationOverlay);
                    booLokace = false;
                }
            }
        });
    }

    /*MENU*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_layers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch  (item.getItemId()) {
            case R.id.subitem1:
                tileSource = TileSourceFactory.MAPNIK;
                map.setTileSource(tileSource);
                return true;
            case R.id.subitem2:
                tileSource = TileSourceFactory.OpenTopo;
                map.setTileSource(tileSource);
                return true;
            case R.id.subitem3:
                tileSource = TileSourceFactory.USGS_SAT;
                map.setTileSource(tileSource);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Opravdu chcete opustit Offline Prohlížeč?")
                .setCancelable(false)
                .setPositiveButton("Ano", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Offline.this.finish();
                    }
                })
                .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    /*PERMISSIONS PRO JISTOTU*/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}
