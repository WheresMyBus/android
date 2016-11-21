package com.wheresmybus;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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

public class RouteMapActivity extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private Route route;

    private final LatLng SEATTLE = new LatLng(47.608013, -122.335167);
    private final float MARKER_HUE = 288;           // makes the bus markers purple
    private final float ZOOM_LEVEL = 18;                // goes up to 21
    private final int REQUEST_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestUserLocationPermission();
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        route = (Route) intent.getSerializableExtra("ROUTE");
    }

    @Override
    public void onPause() {
        super.onPause();

        // stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        try {
            busLocationRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkUserLocationPermission()) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    private void busLocationRequest() throws Exception {
        WMBController controller = WMBController.getInstance();
        controller.getBuses(route.getId(), new Callback<List<Bus>>() {
            @Override
            public void onResponse(Response<List<Bus>> response, Retrofit retrofit) {
                List<Bus> buses = response.body();

                if (buses.isEmpty()) {
                    Toast.makeText(RouteMapActivity.this,
                            "No buses are currently running on this route.",
                            Toast.LENGTH_LONG);
                } else {
                    for (Bus bus : buses) {
                        LatLng busLocation = new LatLng(bus.getLat(), bus.getLon());

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(busLocation);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(MARKER_HUE));

                        mMap.addMarker(markerOptions);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (checkUserLocationPermission()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }


        // place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        /*
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(USER_MARKER_HUE));

        mCurrLocationMarker = mMap.addMarker(markerOptions);
        */

        // move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL));

        // stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private void requestUserLocationPermission() {
        if (!checkUserLocationPermission()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this,
                        "Location permission is needed to show your location on the map.",
                        Toast.LENGTH_LONG).show();
            }

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
    }

    /**
     * Returns true if the user has granted access to their location or false otherwise.
     *
     * @return
     */
    private boolean checkUserLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkUserLocationPermission()) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }

                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    // add marker on Seattle
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(SEATTLE);

                    mCurrLocationMarker = mMap.addMarker(markerOptions);

                    // move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(SEATTLE));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL));
                }

                return;
            }
        }
    }
}
