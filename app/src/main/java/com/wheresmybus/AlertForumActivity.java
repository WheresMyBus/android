package com.wheresmybus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adapters.NeighborhoodAlertAdapter;
import adapters.RouteAlertAdapter;
import controllers.WMBController;
import modules.NeighborhoodAlert;
import modules.RouteAlert;
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

    /**
     * Displays the alerts for the route or neighborhood that was clicked on
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_forum);

        alertList = (ListView) findViewById(R.id.alert_list);
        alertList.setOnItemClickListener(this);

        Intent intent = getIntent();
        String type = intent.getStringExtra("ALERT_TYPE");
        if (type.equals("Route")) {
            isRouteForum = true;
            String number = intent.getStringExtra("ROUTE_NUMBER");
            try {
                routeRequest(number);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else { //neighborhood
            isRouteForum = false;
            int id = intent.getIntExtra("NEIGHBORHOOD_ID", 0);
            try {
                neighborhoodRequest(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the alerts for a given neighborhood from the database
     * @param neighborhoodId the id of the neighborhood to get alerts for
     * @throws Exception if the request fails
     */
    private void neighborhoodRequest(int neighborhoodId) throws Exception {
        WMBController controller = WMBController.getInstance();
        controller.getNeighborhoodAlerts(neighborhoodId, new Callback<List<NeighborhoodAlert>>() {
            @Override
            public void onResponse(Response<List<NeighborhoodAlert>> response, Retrofit retrofit) {
                List<NeighborhoodAlert> data = response.body();
                for (NeighborhoodAlert datum : data) {
                    Log.d("date in onResponse: ", datum.getDate().toString());
                }
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
     * @param routeNumber the number of the route to get alerts for (as a String)
     * @throws Exception if the request fails
     */
    private void routeRequest(String routeNumber) throws Exception {
        WMBController controller = WMBController.getInstance();
        controller.getRouteAlerts(routeNumber, new Callback<List<RouteAlert>>() {
            @Override
            public void onResponse(Response<List<RouteAlert>> response, Retrofit retrofit) {
                List<RouteAlert> data = response.body();
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
     * @param data the list of alerts to be loaded
     */
    private void loadNeighborhoodData(List<NeighborhoodAlert> data) {
        for (NeighborhoodAlert datum : data) {
            Log.d("date in loadData: ", datum.getDate().toString());
        }
        NeighborhoodAlertAdapter adapter = new NeighborhoodAlertAdapter(this, android.R.layout.simple_list_item_1, data);
        alertList.setAdapter(adapter);
    }

    /**
     * Load the given route alerts into the ListView
     * @param data the list of alerts to be loaded
     */
    private void loadRouteData(List<RouteAlert> data) {
        RouteAlertAdapter adapter = new RouteAlertAdapter(this, android.R.layout.simple_list_item_1, data);
        alertList.setAdapter(adapter);
    }

    /**
     * When an alert is clicked, it takes the user to the corresponding
     * alert page
     * @param adapterView the AdapterView that keeps track of the ListView elements
     * @param view the ListView for this class
     * @param position the position of the element clicked
     * @param id the id of the element clicked
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        // send to page for alert that was clicked
        if (isRouteForum) {
            Intent intent = new Intent(this, RouteAlertActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, NeighborhoodAlertActivity.class);
            startActivity(intent);
        }
    }
}
