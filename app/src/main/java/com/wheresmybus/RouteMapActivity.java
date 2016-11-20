package com.wheresmybus;

import android.*;
import android.Manifest;
import android.content.Intent;
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

import java.util.List;

import controllers.WMBController;
import modules.Bus;
import modules.Route;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


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
    private Route route;
    private boolean noBusLocationsFound;

    private final int MAP_TYPE = GoogleMap.MAP_TYPE_NORMAL;
    private final LatLng SEATTLE = new LatLng(47.608013, -122.335167);
    private final float BUS_MARKER_HUE = 288;           // makes the bus markers purple
    private final float USER_MARKER_HUE = 205;          // makes the user marker blue
    private final float ZOOM_LEVEL = 15;                // goes up to 21
    private final int REQUEST_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        route = (Route) intent.getSerializableExtra("ROUTE");

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
        markZoomLocation();
        /*
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.

        // set a marker at the user's location and move the camera there
        */

        // get bus stops, add markers for each, and make them clickable

        // make some kind of view for list of routes that stop at that bus stop to show up if user
        // clicks there

        try {
            busLocationRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (noBusLocationsFound) {
            Toast.makeText(this, "No buses were found to be currently running this route.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpMap() {

    }

    private void busLocationRequest() throws Exception {
        WMBController controller = WMBController.getInstance();
        controller.getBuses(route.getId(), new Callback<List<Bus>>() {
            @Override
            public void onResponse(Response<List<Bus>> response, Retrofit retrofit) {
                // for each bus, add a marker to the map
                String title = route.getNumber() + ": " + route.getName();
                List<Bus> buses = response.body();
                if (buses == null) {
                    noBusLocationsFound = true;
                } else {
                    noBusLocationsFound = false;
                    for (Bus bus : response.body()) {
                        LatLng busLocation = new LatLng(bus.getLat(), bus.getLon());
                        mMap.addMarker(new MarkerOptions().position(busLocation).title(title)
                                .icon(BitmapDescriptorFactory.defaultMarker(BUS_MARKER_HUE)));
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (checkUserLocationPermission()) {
            userLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            // received permission result for location permission

            // check permissions
            // if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (checkUserLocationPermission()) {        // TODO: check if this actually works
                mMap.setMyLocationEnabled(true);
                markUserLocation();
            } else {
                // inform the user that location permission was not granted
                Toast.makeText(this, "Location permission was not granted.", Toast.LENGTH_SHORT).show();

                // display a marker for Seattle instead of the user's current location
                mMap.moveCamera(CameraUpdateFactory.newLatLng(SEATTLE));
                mMap.addMarker(new MarkerOptions().position(SEATTLE).title("Seattle")
                        .icon(BitmapDescriptorFactory.defaultMarker(USER_MARKER_HUE)));
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
        markUserLocation();
    }

    private void markZoomLocation() {
        // check if the user has granted permission to use location data
        if (checkUserLocationPermission() && userLocation != null) {
            // get the user's location and add a marker to it
            mMap.setMyLocationEnabled(true);
            markUserLocation();
        }
    }

    private void markUserLocation() {
        LatLng user = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(user).title("Your current location")
                .icon(BitmapDescriptorFactory.defaultMarker(USER_MARKER_HUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(user));
    }
}
