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

public class SearchRouteMapActivity extends FragmentActivity implements OnMapReadyCallback,
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
    private final float MARKER_HUE = 288;               // makes the bus markers purple
    private final float ZOOM_LEVEL = 18;                // goes up to 21
    private final int REQUEST_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_route_map);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestUserLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // TODO: call method that gets bus stop locations and add clickable markers for each

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

    // TODO: change to a request for bus stop locations
    /*
    private void busLocationRequest() throws Exception {
        WMBController controller = WMBController.getInstance();
        controller.getBuses(route.getId(), new Callback<List<Bus>>() {
            @Override
            public void onResponse(Response<List<Bus>> response, Retrofit retrofit) {
                List<Bus> buses = response.body();

                if (buses.isEmpty()) {
                    Toast.makeText(SearchRouteMapActivity.this,
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
     * @return
     */
    private boolean checkUserLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
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

    private class BusStopListener implements GoogleMap.OnMarkerClickListener, ListView.OnItemClickListener {
        private Map<Marker, BusStop> markerBusStopMap;
        private RouteAdapter routeAdapter;

        /**
         * Constructs a new BusStopListener.
         *
         * @param markerBusStopMap a map that matches markers to the bus stops they represent
         */
        public BusStopListener(Map<Marker, BusStop> markerBusStopMap) {
            if (markerBusStopMap != null) {
                this.markerBusStopMap = markerBusStopMap;
            } else {
                markerBusStopMap = new HashMap<>();
            }
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Route route = (Route) parent.getItemAtPosition(position);
            Intent intent = new Intent(view.getRootView().getContext(), AlertForumActivity.class);
            intent.putExtra("ALERT_TYPE", "Route");
            intent.putExtra("ROUTE", route);
            intent.putExtra("ROUTE_ID", route.getId());
            startActivity(intent);
        }

        @Override
        public boolean onMarkerClick(Marker marker) {
            BusStop busStop = getBusStop(marker);
            if (busStop != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchRouteMapActivity.this);
                builder.setTitle("Routes that stop here:");         // TODO: get bus stop name
                try {
                    ListView routesList = new ListView(SearchRouteMapActivity.this);
                    if (routeAdapter == null) {
                        // TODO: see if these should be starred
                        // TODO: get actual method for getting the routes associated with this stop
                        routeAdapter = new RouteAdapter(SearchRouteMapActivity.this,
                                android.R.layout.simple_list_item_1,
                                new ArrayList<>(busStop.getRoutes()), false);
                    }
                    routesList.setAdapter(routeAdapter);
                    routesList.setOnItemClickListener(this);
                    builder.setView(routesList);
                    builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                builder.show();

                return false;   // the camera should move to the marker and an info window should appear
            } else {
                return true;    // the camera should not move to the marker and no info window appear
            }
        }

        private BusStop getBusStop(Marker marker) {
            return markerBusStopMap.get(marker);
        }
    }
}
