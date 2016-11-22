package com.wheresmybus;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import adapters.NeighborhoodAlertAdapter;
import adapters.RouteAlertAdapter;
import controllers.WMBController;
import modules.Alert;
import modules.Neighborhood;
import modules.NeighborhoodAlert;
import modules.Route;
import modules.RouteAlert;
import modules.UserDataManager;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by bmartz on 11/11/2016.
 *
 * A class for the alert forum screen, for either a route or a neighborhood
 */
public class AlertForumActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView alertList;
    private boolean isRouteForum; // true if routes, false if neighborhoods
    private Route route;
    private Neighborhood neighborhood;
    private Button viewLocationsButton;

    /**
     * Displays the alerts for the route or neighborhood that was clicked on
     *
     * @param savedInstanceState previously saved state, or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_forum);

        alertList = (ListView) findViewById(R.id.alert_list);
        alertList.setOnItemClickListener(this);

        viewLocationsButton = (Button) findViewById(R.id.view_locations_button);

        Intent intent = getIntent();
        String type = intent.getStringExtra("ALERT_TYPE");
        // Route alerts
        if (type.equals("Route")) {
            isRouteForum = true;
            route = (Route) intent.getSerializableExtra("ROUTE");
            // set page title
            setTitle(route.getNumber() + ": " + route.getName());
            String id = intent.getStringExtra("ROUTE_ID");
            // get and load alerts for the route from database
            try {
                routeRequest(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else { // Neighborhood alerts
            isRouteForum = false;
            neighborhood = (Neighborhood) intent.getSerializableExtra("NEIGHBORHOOD");
            // set page title
            setTitle(neighborhood.getName());
            int id = intent.getIntExtra("NEIGHBORHOOD_ID", 0);
            // get and load alerts for the neighborhood from database
            try {
                neighborhoodRequest(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // make view bus locations button invisible
            viewLocationsButton.setVisibility(View.INVISIBLE);
        }
    }

    // TODO: saving user data writes all of the user data to files, which could be unnecessarily
    // expensive to perform as often as we will be doing - Nick B. (though the files are small
    // enough that it probably won't matter
    @Override
    public void onStop() {
        super.onStop();
        UserDataManager.getManager().saveUserData(this);
    }

    /**
     * Gets the alerts for a given neighborhood from the database
     *
     * @param neighborhoodId the id of the neighborhood to get alerts for
     * @throws Exception if the request fails
     */
    private void neighborhoodRequest(int neighborhoodId) throws Exception {
        WMBController controller = WMBController.getInstance();
        controller.getNeighborhoodAlerts(neighborhoodId, new Callback<List<NeighborhoodAlert>>() {
            @Override
            public void onResponse(Response<List<NeighborhoodAlert>> response, Retrofit retrofit) {
                List<NeighborhoodAlert> data = response.body();
                Collections.sort(data);
                loadNeighborhoodData(data);
            }

            @Override
            public void onFailure(Throwable t) {
                // stuff to do when it doesn't work
            }
        });
    }

    /**
     * Gets the alerts for a given route from the database
     *
     * @param routeId the id of the route to get alerts for (as a String)
     * @throws Exception if the request fails
     */
    private void routeRequest(String routeId) throws Exception {
        WMBController controller = WMBController.getInstance();
        controller.getRouteAlerts(routeId, new Callback<List<RouteAlert>>() {
            @Override
            public void onResponse(Response<List<RouteAlert>> response, Retrofit retrofit) {
                List<RouteAlert> data = response.body();
                Collections.sort(data);
                loadRouteData(data);
            }

            @Override
            public void onFailure(Throwable t) {
                // stuff to do when it doesn't work
            }
        });
    }

    /**
     * Load the given neighborhood alerts into the ListView
     *
     * @param data the list of alerts to be loaded
     */
    private void loadNeighborhoodData(List<NeighborhoodAlert> data) {
        NeighborhoodAlertAdapter adapter = new NeighborhoodAlertAdapter(this, android.R.layout.simple_list_item_1, data);
        alertList.setAdapter(adapter);
    }

    /**
     * Load the given route alerts into the ListView
     *
     * @param data the list of alerts to be loaded
     */
    private void loadRouteData(List<RouteAlert> data) {
        RouteAlertAdapter adapter = new RouteAlertAdapter(this, android.R.layout.simple_list_item_1, data);
        alertList.setAdapter(adapter);
    }

    /**
     * When an alert is clicked, it takes the user to the corresponding
     * alert page
     *
     * @param adapterView the AdapterView that keeps track of the ListView elements
     * @param view the ListView for this class
     * @param position the position of the element clicked
     * @param id the id of the element clicked
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        // send to page for alert that was clicked
        Alert alert = (Alert) adapterView.getItemAtPosition(position);
        Intent intent;
        if (isRouteForum) {
            intent = new Intent(this, RouteAlertActivity.class);
        } else {
            intent = new Intent(this, NeighborhoodAlertActivity.class);
        }
        intent.putExtra("ALERT", alert);
        startActivity(intent);
    }

    /**
     * Sends the user to the submit alert screen
     *
     * @param v The current view
     */
    public void switchToSubmitAlert(View v) {
        Intent intent = new Intent(this, SubmitAlertActivity.class);
        intent.putExtra("ROUTE", route);
        intent.putExtra("NEIGHBORHOOD", neighborhood);
        startActivity(intent);
    }

    /**
     * Sends the user to the screen where users can view the current locations of the buses running
     * the route whose forum is currently being viewed.
     *
     * @param view the current view
     */
    public void switchToViewLocations(View view) {
        Intent intent = new Intent(this, RouteMapActivity.class);
        intent.putExtra("ROUTE", route);
        startActivity(intent);
    }
}
