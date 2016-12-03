package com.wheresmybus;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import controllers.WMBController;
import modules.Bus;
import modules.Route;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

// for latitude, positive ints are north; for longitude, positive ints are east

/**
 * A map that displays and zooms in on the user's location if permissions are enabled, or otherwise
 * zooms in on Seattle, and then displays the current locations of buses running a given route.
 */
public class RouteMapActivity extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    // private fields
    private GoogleMap mMap;                     // the map
    private SupportMapFragment mapFragment;     // the fragment storing the map
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;   // tool to help track the user's current location
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private Route route;                        // the route whose bus locations are being viewed
    private Button refreshButton;               // the button that lets users refresh the markers
    private List<Marker> currentBusMarkers;     // the bus locations currently marked

    private final LatLng SEATTLE = new LatLng(47.608013, -122.335167);
    private final float MARKER_HUE = 288;               // makes the bus markers purple
    private final float ZOOM_LEVEL = 14;                // goes up to 21
    private final int REQUEST_LOCATION = 0;

    /**
     * Part of the call structure to display the activity.
     *
     * @param savedInstanceState previously saved state, or null
     */
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

        refreshButton = (Button) findViewById(R.id.refresh_button);
        refreshButton.setVisibility(View.INVISIBLE);

        currentBusMarkers = new ArrayList<>();
    }

    /**
     * Pauses the activity and stops updating the user's current location.
     */
    @Override
    public void onPause() {
        super.onPause();

        // stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    /**
     * Stops the activity and disconnects the tool that helps track the user's current location.
     */
    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    /**
     * Stores the map once it is ready, adds the user's current location if the user's allows the
     * permission or sets the map camera to Seattle, adds markers to the map to indicate the
     * locations of the buses running the route for this page, and makes the refresh button visible.
     *
     * @param googleMap the map being displayed
     */
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

        refreshButton.setVisibility(View.VISIBLE);
    }

    /**
     * Sets up and connects the tool that helps track the user's current location.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    /**
     * Requests the list of buses that are running the route for this page. Reports to the user if
     * there are no buses currently running this route or adds markers to indicate the locations of
     * all buses running it.
     *
     * @throws Exception
     */
    private void busLocationRequest() throws Exception {
        WMBController controller = WMBController.getInstance();
        controller.getBuses(route.getId(), new Callback<List<Bus>>() {
            @Override
            public void onResponse(Response<List<Bus>> response, Retrofit retrofit) {
                List<Bus> buses = response.body();

                if (buses.isEmpty()) {
                    // report that no buses are running this route
                    Toast.makeText(RouteMapActivity.this,
                            "No buses are currently running on this route.",
                            Toast.LENGTH_LONG);
                } else {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();

                    for (Bus bus : buses) {
                        LatLng busLocation = new LatLng(bus.getLat(), bus.getLon());

                        // sets up a marker for the bus location
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(busLocation);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(MARKER_HUE));

                        // adds the marker to the map and saves a reference to it
                        Marker marker = mMap.addMarker(markerOptions);
                        currentBusMarkers.add(marker);

                        builder.include(busLocation);
                    }

                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * Sets up the request for the user location.
     *
     * @param bundle
     */
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

    /**
     * Gets the user's current location if they have moved.
     *
     * @param location the user's new location
     */
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

    /**
     * Requests permission to access the user's location if not already granted.
     */
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
     * @return true if the user has granted permissions to access their current location or false
     *              otherwise
     */
    private boolean checkUserLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * If the given request code corresponds to our request to access the user's location, gets the
     * user's current location and adds it to the map if the user granted permission or adds a marker
     * and zooms to Seattle.
     *
     * @param requestCode the code to indicate which permission result is being analyzed
     * @param permissions the permissions requested
     * @param grantResults the results of requesting the permissions
     */
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

    /**
     * Clears the markers that indicate the current locations of the buses running this route, gets
     * new information about the buses, and displays markers for the updated bus locations.
     *
     * @param view the button clicked
     */
    public void refresh(View view) {
        clearCurrentBusMarkers();
        try {
            busLocationRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes all the bus location markers from the map and any references to them in this class.
     */
    public void clearCurrentBusMarkers() {
        for (Marker m : currentBusMarkers) {
            m.remove();
        }
        currentBusMarkers = new ArrayList<>();
    }
}
