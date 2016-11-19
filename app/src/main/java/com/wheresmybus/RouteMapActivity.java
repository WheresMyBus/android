package com.wheresmybus;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


// shouldShowRequestPermissionsRationale(): returns true if app has requested permission before and
//          user denied the report - returns false if user has requested permission previously and
//          chose Don't Ask Again option or device policy prohibits app from having that permission

// for latitude, positive ints are north; for longitude, positive ints are east
public class RouteMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location userLocation;
    private boolean haveRequestedPermission;
    private boolean havePermission;

    private final int MAP_TYPE = GoogleMap.MAP_TYPE_NORMAL;
    private final LatLng SEATTLE = new LatLng(47.608013, -122.335167);
    private final float MARKER_HUE = 288;           // makes the markers purple
    private final float ZOOM_LEVEL = 15;            // goes up to 21
    private final int REQUEST_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // set up GoogleApiClient which helps track user location
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        }

        haveRequestedPermission = false;        // default value for boolean
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(MAP_TYPE);
        if (checkUserLocationPermission() && userLocation != null) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            // set a marker at the user's location and move the camera there
            mMap.setMyLocationEnabled(true);
            LatLng user = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(user).title("Your current location")
                    .icon(BitmapDescriptorFactory.defaultMarker(MARKER_HUE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(user));
        } else {
            // zoom to Seattle
            Toast toast = Toast.makeText(this, "Please consider changing your permissions.", Toast.LENGTH_SHORT);
            toast.show();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(SEATTLE));
            mMap.addMarker(new MarkerOptions().position(SEATTLE).title("Seattle").icon(BitmapDescriptorFactory.defaultMarker(MARKER_HUE)));
        }

        // get bus stops, add markers for each, and make them clickable

        // make some kind of view for list of routes that stop at that bus stop to show up if user
        // clicks there
        /*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
        setUpMap();
    }

    private void setUpMap() {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (checkUserLocationPermission()) {
            userLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Returns true if the user has granted access to their location or false otherwise.
     *
     * @return
     */
    private boolean checkUserLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;

        /*
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (!haveRequestedPermissions) {
                // ask the user for permission to access their location
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                return havePermissions;
            } else {
                // note that we have asked for permission already and return false to indicate we
                // do not have the user's permission
                haveRequestedPermissions = true;
                return false;
            }
        }
        */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            // received permission result for camera permission

            // check permissions
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                markUserLocation();             // does this cause an infinite loop?
            } else {
                Toast.makeText(this, "Location permission was not granted.", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // remove marker for old location from map

        // set user location to given location
        userLocation = location;

        // add marker for current location

    }

    private void markUserLocation() {
        // check if the user has granted permission to use location data
        if (checkUserLocationPermission()) {
            // get the user's location and add a marker to it
            mMap.setMyLocationEnabled(true);
            LatLng user = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(user).title("Your current location")
                    .icon(BitmapDescriptorFactory.defaultMarker(MARKER_HUE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(user));
        } else {
            // provide rationale to the user of benefit to granting permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Location permission is needed to show the user's location.",
                        Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

            }
        }
    }
}
