package com.wheresmybus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import controllers.WMBController;
import modules.Neighborhood;
import modules.NeighborhoodAdapter;
import modules.NeighborhoodAlert;
import modules.RouteAlert;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by bmartz on 11/11/2016.
 */
public class AlertForumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_forum);

        Intent intent = getIntent();
        String type = intent.getStringExtra("ALERT_TYPE");
        if (type.equals("Route")) {
            String number = intent.getStringExtra("ROUTE_NUMBER");
            try {
                routeRequest(number);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else { //neighborhood
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
        //NeighborhoodAdapter adapter = new NeighborhoodAdapter(this, android.R.layout.simple_list_item_1, data);
        //neighborhoodList.setAdapter(adapter);
    }

    /**
     * Load the given route alerts into the ListView
     * @param data the list of alerts to be loaded
     */
    private void loadRouteData(List<RouteAlert> data) {

    }
}
