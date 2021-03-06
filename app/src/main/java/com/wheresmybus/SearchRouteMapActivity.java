package com.wheresmybus;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.RouteAdapter;
import controllers.WMBController;
import modules.Bus;
import modules.BusStop;
import modules.Route;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * The map screen that allows a user to search for a route forum by map. The map sets markers for
 * bus stops. When a user clicks on a marker, it pulls up a dialog that lists the routes that stop
 * at that bus stop. Users can then click on a route, which will direct them to the forum for that
 * route.
 */
public class SearchRouteMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;                     // the map
    private SupportMapFragment mapFragment;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;   // a tool that helps track the user's location
    private Location mLastLocation;
    private Marker mCurrLocationMarker;

    private Map<Marker, BusStop> markerBusStopHashMap = new HashMap<>();

    private final LatLng SEATTLE = new LatLng(47.608013, -122.335167);
    private final float MARKER_HUE = 205;               // makes the bus markers purple
    private final float ZOOM_LEVEL = 18;                // goes up to 21
    private final int REQUEST_LOCATION = 0;

    /**
     * Part of the call structure that displays the activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_route_map);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestUserLocationPermission();
        }

        buildGoogleApiClient();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Pauses the activity and stops receiving updates on the user's location.
     */
    @Override
    public void onPause() {
        super.onPause();

        // stop location updates when Activity is no longer active
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    /**
     * Connects to the user location service when the activity starts.
     */
    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    /**
     * Stops the activity and disconnects the tool that tracks the user's location.
     */
    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Manipulates the map once available. This callback is triggered when the map is ready to be
     * used. Marks the user's current location if the user has granted permission or otherwise
     * requests the user to grant location permissions.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                Log.d("TEST", "CAMERA STOPPED");

                try {
                    busStopsRequest(mMap.getCameraPosition().target);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mMap.setOnMarkerClickListener(new BusStopListener(markerBusStopHashMap));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkUserLocationPermission()) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            mMap.setMyLocationEnabled(true);
        }
    }

    /**
     * Builds the tool that helps track the user's current location.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Gets bus stops near a given location and puts a marker for each of them on the map.
     * @param location the location around which to search for bus stops
     * @throws Exception
     */
    private void busStopsRequest(LatLng location) throws Exception {
        WMBController controller = WMBController.getInstance();
        controller.getBusStops(location.latitude, location.longitude, 300, new Callback<List<BusStop>>() {
            @Override
            public void onResponse(Response<List<BusStop>> response, Retrofit retrofit) {
                List<BusStop> busStops = response.body();

                if (!busStops.isEmpty()) {
                    for (BusStop busStop : busStops) {
                        if (!markerBusStopHashMap.containsValue(busStop)) {
                            LatLng busStopLocation = new LatLng(busStop.getLat(), busStop.getLon());

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(busStopLocation);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(MARKER_HUE));

                            Marker marker = mMap.addMarker(markerOptions);

                            markerBusStopHashMap.put(marker, busStop);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {}
        });
    }

    /**
     * Sets up the request for the user's location.
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
     * @param location the user's current location
     */
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        // place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

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
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this,
                        "Location permission is needed to show your location on the map.",
                        Toast.LENGTH_LONG).show();
            }

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
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
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
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
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    // move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(SEATTLE));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL));
                }

                return;
            }
        }
    }

    /**
     * A listener for the markers on the map that shows bus stops. When a user clicks on the marker
     * for a bus stop, a dialog appears that shows a list of routes that stop at that bus stop. The
     * user can then click on a route to navigate to the forum for that route.
     */
    private class BusStopListener implements GoogleMap.OnMarkerClickListener, ListView.OnItemClickListener {
        private Map<Marker, BusStop> markerBusStopMap;  // the map of markers to bus

        /**
         * Constructs a new BusStopListener.
         *
         * @param markerBusStopMap a map that matches markers to the bus stops they represent
         */
        public BusStopListener(Map<Marker, BusStop> markerBusStopMap) {
            if (markerBusStopMap != null) {
                this.markerBusStopMap = markerBusStopMap;
            } else {
                this.markerBusStopMap = new HashMap<>();
            }
        }

        /**
         * When a user clicks on a route in the dialog that appears after clicking on a bus stop
         * marker, the user is sent to the forum for that route.
         *
         * @param parent the list view
         * @param view the view of the row for the given route
         * @param position the position of the row within the list view
         * @param id the ID of the row
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Route route = (Route) parent.getItemAtPosition(position);
            Intent intent = new Intent(view.getRootView().getContext(), AlertForumActivity.class);
            intent.putExtra("ALERT_TYPE", "Route");
            intent.putExtra("ROUTE", route);
            intent.putExtra("ROUTE_ID", route.getId());
            startActivity(intent);
        }

        /**
         * When a user clicks on the given marker, a dialog appears that displays a list of the
         * routes that stops at that bus stop. If no bus stop was associated with the given marker,
         * nothing happens.
         *
         * @param marker the marker clicked
         * @return false if the dialog appears or true otherwise
         */
        @Override
        public boolean onMarkerClick(Marker marker) {
            BusStop busStop = getBusStop(marker);
            if (busStop != null) {
                // set up the dialog
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(SearchRouteMapActivity.this,
                            android.R.style.Theme_Material_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(SearchRouteMapActivity.this);
                }
                builder.setTitle(busStop.getName());

                // set up the list view of routes
                try {
                    ListView routesList = new ListView(SearchRouteMapActivity.this);
                    List<Route> routes = busStop.getRoutes();
                    Collections.sort(routes);
                    RouteAdapter routeAdapter = new RouteAdapter(SearchRouteMapActivity.this,
                            android.R.layout.simple_list_item_1, routes, false);
                    routesList.setAdapter(routeAdapter);
                    routesList.setOnItemClickListener(this);
                    builder.setView(routesList);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // add a button to close the dialog and display
                builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();

                return false;   // the camera should move to the marker and an info window should appear
            } else {
                return true;    // the camera should not move to the marker and no info window appear
            }
        }

        /**
         * Returns the bus stop associated with the given marker or null if there is no associated
         * bus stop.
         *
         * @param marker the marker for which an associated bus stop is to be retrieved
         * @return the bus stop associated with the given marker or null if there is none
         */
        private BusStop getBusStop(Marker marker) {
            return markerBusStopMap.get(marker);
        }
    }
}
