package com.wheresmybus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import controllers.WMBController;
import modules.Neighborhood;
import modules.NeighborhoodAdapter;
import modules.NeighborhoodAlert;
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
            String id = intent.getStringExtra("ROUTE_ID");
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
     * @throws Exception if the request fails
     */
    private void neighborhoodRequest(int neighborhoodId) throws Exception {
        WMBController controller = WMBController.getInstance();
        controller.getNeighborhoodAlerts(neighborhoodId, new Callback<List<NeighborhoodAlert>>() {
            @Override
            public void onResponse(Response<List<NeighborhoodAlert>> response, Retrofit retrofit) {
                List<NeighborhoodAlert> data = response.body();
                loadListData(data);
            }

            @Override
            public void onFailure(Throwable t) {
                // stuff to do when it doesn't work
            }
        });
    }

    /**
     * Load the given data into the ListView
     * @param data the list of alerts to be loaded
     */
    private void loadListData(List<NeighborhoodAlert> data) {
        //NeighborhoodAdapter adapter = new NeighborhoodAdapter(this, android.R.layout.simple_list_item_1, data);
        //neighborhoodList.setAdapter(adapter);
    }
}
