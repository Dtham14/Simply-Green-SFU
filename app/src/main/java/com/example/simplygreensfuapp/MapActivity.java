package com.example.simplygreensfuapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapActivity extends AppCompatActivity
        implements LocationListener {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private MapController mapController;
    private MyLocationNewOverlay myLocationNewOverlay;
    private GpsMyLocationProvider gpsMyLocationProvider;
    private LocationManager locationManager;
    private SharedPreferences filterPreference;
    private String[] waypointTypes;
    private List<Waypoint> waypointList = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //handle permissions first, before map is created. not depicted here
        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's
        //tile servers will get you banned based on this string


        //initiate SharedPreference and Intent
        filterPreference = getApplicationContext().getSharedPreferences("filterPreferences", Context.MODE_PRIVATE);
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        //inflate and create the map
        setContentView(R.layout.activity_map);

        //initiate mapView
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);
        mapController = new MapController(map);

        //osmdroid MyLocationNewOverlay
        try {
            gpsMyLocationProvider = new GpsMyLocationProvider(ctx);
            myLocationNewOverlay = new MyLocationNewOverlay(gpsMyLocationProvider, map);
            myLocationNewOverlay.enableMyLocation();
        } catch (Exception e) {
            myLocationNewOverlay = null;
        }

        //initiate Waypoints and filter
        waypointList = new ArrayList<>();
        waypointTypes = getResources().getStringArray(R.array.waypoint_types);
        if (filterPreference.getAll().entrySet().size() == 0) {
            SharedPreferences.Editor editor = filterPreference.edit();
            for (String waypointType : waypointTypes) {
                editor.putBoolean(waypointType, true);
            }
            editor.apply();
        }

        centerCampus();
        loadWaypoints();
        nearestWaypoint();
    }

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up

        filterWaypoints();
        //need to add location overlay here
        if (myLocationNewOverlay != null) map.getOverlays().add(myLocationNewOverlay);

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

    //Required to get location. Not used
    @Override
    public void onLocationChanged(@NonNull Location location) {

    }


    /**
     * Function to center map and zoom into campus
     */
    public void centerCampus() {
        //location of the center of SFU burnaby campus
        GeoPoint gPt = new GeoPoint(49.2770, -122.9200);
        mapController.setCenter(gPt);
        //zoom into map
        mapController.setZoom(16);
    }

    public void centerCampus(View view) {
        centerCampus();
    }

    /**
     * Function to open Filter Activity
     *
     * @param view View
     */
    public void openFilter(View view) {
        Intent newIntent = new Intent(this, FilterActivity.class);
        startActivity(newIntent);
    }

    /**
     * Function to draw waypoints if the it is enabled
     */
    private void filterWaypoints() {
        //Clear all overlays
        map.getOverlays().clear();
        ArrayList<String> filterList = new ArrayList<>();
        for (String waypointType : waypointTypes) {
            if (filterPreference.getBoolean(waypointType, true)) {
                filterList.add(waypointType);
            }
        }
        //cycle through each waypoint and check if its type matches and object in the filter list
        for (int i = 0; i < waypointList.size(); i++) {
            Waypoint waypoint = (Waypoint) waypointList.get(i);
            String type = waypoint.getType();
            //if the waypoint type matches an object in the filter list or if the filter list contains the object all, display waypoint
            if (filterPreference.getBoolean(type, false)) {
                map.getOverlays().add(waypoint.getMarker());
            }
        }
    }

    /**
     * Function to zoom into the nearest waypoint to the user
     */
    private void nearestWaypoint() {
        //only focus when theres one filter
        Log.d("TAG", filterPreference.getAll().size() + " " + filterPreference.getAll().containsKey(""));

        if (filterPreference.getAll().entrySet().size() == 1) {

            Waypoint nearest = null;
            Waypoint next = null;

            int i = 0;
            while (i < waypointList.size()) {
                next = waypointList.get(i);
                if (filterPreference.getAll().containsKey(waypointList.get(i).getType())) {
                    if (nearest == null) nearest = next;
                    else nearest = compareDistance(nearest, next);
                }
                i++;
            }

            if (nearest != null) {
                mapController.setCenter(nearest.getGeoPoint());
                mapController.setZoom(19);
            }


        }
    }

    /**
     * Compare distance from GPS location to two Waypoints and return the nearest one
     *
     * @param a Waypoint a
     * @param b Waypoint b
     * @return Waypoint
     */
    private Waypoint compareDistance(Waypoint a, Waypoint b) {
        //location of the center of SFU burnaby campus
        try {
            GeoPoint user;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                user = new GeoPoint(49.2770, -122.9200);
            } else {
                user = new GeoPoint(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(), locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude());
            }
            if (user.distanceToAsDouble(a.getGeoPoint()) < user.distanceToAsDouble(b.getGeoPoint()))
                return a;
            else
                return b;
        } catch (Exception e) {
            return a;
        }
    }

    /**
     * Function to create all waypoints and add them to the waypoint list
     */
    private void loadWaypoints() {
        for (String waypointType : waypointTypes) {
            int coordinatesId, waypointNameId, drawableId;
            try {
                coordinatesId = R.array.class.getField("waypoint_coordinates_" + waypointType).getInt(null);
                waypointNameId = R.string.class.getField("waypoint_title_" + waypointType).getInt(null);
                drawableId = R.drawable.class.getField("waypoint_" + waypointType.toLowerCase()).getInt(null);
            } catch (Exception e) {
                Log.e("Resource id", "Failed to get id.", e);
                return;
            }
            String[] coordinatesString = getResources().getStringArray(coordinatesId);
            for (String coordinateString : coordinatesString) {
                double[] coordinate = Arrays.stream(coordinateString.split(",")).mapToDouble(Double::parseDouble).toArray();

                Marker marker = new Marker(map);
                GeoPoint geoPoint = new GeoPoint(coordinate[0], coordinate[1]);
                marker.setPosition(geoPoint);
                marker.setTitle(getResources().getString(waypointNameId));
                marker.setIcon(AppCompatResources.getDrawable(getApplicationContext(), drawableId));
                Waypoint waypoint = new Waypoint(marker, waypointType);
                waypointList.add(waypoint);
            }
        }
    }
}