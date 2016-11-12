package com.wheresmybus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.List;

import controllers.WMBController;
import modules.Neighborhood;
import modules.NeighborhoodAlert;
import modules.Route;
import modules.RouteAlert;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by lesli_000 on 11/8/2016.
 */

public class SubmitAlertActivity extends FragmentActivity implements
        BusRouteAlertFragment.OnFragmentInteractionListener,
        NeighborhoodAlertFragment.OnFragmentInteractionListener {
    private Button submitButton;
    private FragmentManager fragmentManager;
    private BusRouteAlertFragment busRouteFragment;
    private NeighborhoodAlertFragment neighborhoodFragment;

    // parameter to get alert information that will be submitted
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_alert);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.bus_route_alert_fragment,
                new BusRouteAlertFragment(), "busRouteFragment").commit();
        busRouteFragment = (BusRouteAlertFragment)
                fragmentManager.findFragmentById(R.id.bus_route_alert_fragment);
        busRouteFragment.getView().setVisibility(View.INVISIBLE);

        fragmentManager.beginTransaction().add(R.id.neighborhood_alert_fragment,
                new NeighborhoodAlertFragment(), "neighborhoodFragment").commit();
        neighborhoodFragment = (NeighborhoodAlertFragment)
                fragmentManager.findFragmentById(R.id.neighborhood_alert_fragment);
        neighborhoodFragment.getView().setVisibility(View.INVISIBLE);

        submitButton = (Button) findViewById(R.id.button_submit);
        submitButton.setVisibility(View.INVISIBLE);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        submitButton.setVisibility(View.VISIBLE);
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_bus_route:
                if (checked)
                    neighborhoodFragment.getView().setVisibility(View.INVISIBLE);
                    busRouteFragment.getView().setVisibility(View.VISIBLE);
                    type = "route";
                    break;
            case R.id.radio_neighborhood:
                if (checked)
                    busRouteFragment.getView().setVisibility(View.INVISIBLE);
                    neighborhoodFragment.getView().setVisibility(View.VISIBLE);
                    type = "neighborhood";
                    break;
        }
    }

    public void onSubmitButtonClicked(View view) {
        // need route/neighborhood ID, alertType string, description of alert, 0, callback
        if (type.equals("route")) {
            // get the information from the BusRouteFragment
            Route route = busRouteFragment.getRoute();
            String alertType = busRouteFragment.getAlertType();
            String description = busRouteFragment.getDescription();

            if (route == null || alertType == null || description == null) {
                // instruct user that some parameter is missing information
                Toast toast = Toast.makeText(this, "Please enter any missing parameters.",
                        Toast.LENGTH_SHORT);
                toast.show();
            } else {
                // submit new route alert
                WMBController controller = WMBController.getInstance();
                controller.postAlert(route.getId(), alertType, description, 0, new Callback<RouteAlert>() {
                    @Override
                    public void onResponse(Response<RouteAlert> response, Retrofit retrofit) {

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });

                // switch back to home screen
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        } else if (type.equals("neighborhood")) {
            // get the information from the NeighborhoodFragment
            Neighborhood neighborhood = neighborhoodFragment.getNeighborhood();
            String alertType = neighborhoodFragment.getAlertType();
            String description = neighborhoodFragment.getDescription();

            if (neighborhood == null || alertType == null || description == null) {
                // instruct the user that some parameter is missing information
                Toast toast = Toast.makeText(this, "Please enter any missing parameters.",
                        Toast.LENGTH_SHORT);
                toast.show();
            } else {
                // submit new alert
                WMBController controller = WMBController.getInstance();
                controller.postAlert(neighborhood.getID(), alertType, description, 0, new Callback<NeighborhoodAlert>() {
                    @Override
                    public void onResponse(Response<NeighborhoodAlert> response, Retrofit retrofit) {

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });

                // switch back to home screen
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
