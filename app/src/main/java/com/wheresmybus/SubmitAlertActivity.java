package com.wheresmybus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import java.util.ArrayList;
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

/**
 * The activity associated with the screen for the Submit Alert page found by clicking the
 * "Submit Alert" button on the home screen.
 */
public class SubmitAlertActivity extends AppCompatActivity implements
        BusRouteAlertFragment.OnFragmentInteractionListener,
        NeighborhoodAlertFragment.OnFragmentInteractionListener {
    // references to the buttons and the fragments used on this screen
    private Button cancelButton;
    private Button submitButton;
    private BusRouteAlertFragment busRouteFragment;
    private NeighborhoodAlertFragment neighborhoodFragment;

    // parameter to get alert information that will be submitted
    private String type;

    /**
     * Part of the call structure to display this activity.
     *
     * @param savedInstanceState previously saved state, or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_alert);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        busRouteFragment = (BusRouteAlertFragment)
                fragmentManager.findFragmentById(R.id.bus_route_alert_fragment);
        busRouteFragment.getView().setVisibility(View.INVISIBLE);

        neighborhoodFragment = (NeighborhoodAlertFragment)
                fragmentManager.findFragmentById(R.id.neighborhood_alert_fragment);
        neighborhoodFragment.getView().setVisibility(View.INVISIBLE);

        cancelButton = (Button) findViewById(R.id.cancel_button);
        //cancelButton.setVisibility(View.INVISIBLE);

        submitButton = (Button) findViewById(R.id.button_submit);
        submitButton.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        boolean isRoute = intent.getBooleanExtra("IS_ROUTE", false);
        Route route = (Route) intent.getSerializableExtra("ROUTE");
        Neighborhood neighborhood = (Neighborhood) intent.getSerializableExtra("NEIGHBORHOOD");
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.type_buttons);
        RadioButton routeRadioButton = (RadioButton) findViewById(R.id.radio_bus_route);
        RadioButton neighborhoodRadioButton = (RadioButton) findViewById(R.id.radio_neighborhood);
        if (isRoute || route != null) {
            // set appropriate radio button and disable both spinner choices and make these non-clickable
            radioGroup.check(R.id.radio_bus_route);
            routeRadioButton.setEnabled(false);
            neighborhoodRadioButton.setEnabled(false);

            type = "route";

            // make bus route fragment visible
            busRouteFragment.getView().setVisibility(View.VISIBLE);

            if (route != null) {
                // set and disable the spinner
                busRouteFragment.setSpinner(route);
            }

            // make submit button visible
            submitButton.setVisibility(View.VISIBLE);
        } else if (neighborhood != null) {
            // set radio button and spinner choices and make these non-clickable
            radioGroup.check(R.id.radio_neighborhood);
            routeRadioButton.setEnabled(false);
            neighborhoodRadioButton.setEnabled(false);

            type = "neighborhood";

            // make neighborhood fragment visible and set and disable the spinner
            neighborhoodFragment.getView().setVisibility(View.VISIBLE);
            neighborhoodFragment.setSpinner(neighborhood);

            // make submit button visible
            submitButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Creates an options menu.
     *
     * @param menu the menu to be inflated
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handles the event when an option is selected from the options menu.
     *
     * @param item The selected item
     * @return false to allow normal menu processing to proceed, true to consume it here
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            Uri uri = Uri.parse("https://github.com/WheresMyBus/android/wiki/User-Documentation");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Loads the BusRouteAlertFragment and makes the NeighborhoodAlertFragment invisible if the bus
     * route radio button was selected. Loads the NeighborhoodAlertFragment and makes the
     * BusRouteAlertFragment invisible if the neighborhood radio button was selected.
     *
     * @param view the current view
     */
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        submitButton.setVisibility(View.VISIBLE);
        // cancelButton.setVisibility(View.VISIBLE);
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

    /**
     * If the user has submitted all of the necessary information, submits the user's alert and
     * returns the user to the homepage. Otherwise, instructs the user to fill out any missing
     * parameters.
     *
     * @param view the button clicked
     */
    public void onSubmitButtonClicked(View view) {
        if (type.equals("route")) {
            // get the information from the BusRouteFragment
            Route route = busRouteFragment.getRoute();
            String alertType = busRouteFragment.getAlertType();
            String otherType = busRouteFragment.getOtherType();
            if (otherType != null) {
                if ((otherType.trim()).equals("")) {
                    otherType = "Other";
                }
                alertType = alertType.replace("Other", otherType);
            }
            String description = busRouteFragment.getDescription();

            if (route == null || alertType == null || description == null || description.equals("")) {
                // instruct user that some parameter is missing information
                Toast toast = Toast.makeText(this, "Please enter any missing parameters.",
                        Toast.LENGTH_SHORT);
                toast.show();
            } else {
                submitButton.setEnabled(false);
                // submit new route alert
                WMBController controller = WMBController.getInstance();
                controller.postAlert(route.getId(), alertType, description, "[User ID]", new Callback<RouteAlert>() {
                    @Override
                    public void onResponse(Response<RouteAlert> response, Retrofit retrofit) {
                        // switch back to previous screen
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        finish();
                    }
                });
            }
        } else if (type.equals("neighborhood")) {
            // get the information from the NeighborhoodFragment
            Neighborhood neighborhood = neighborhoodFragment.getNeighborhood();
            String alertType = neighborhoodFragment.getAlertType();
            String otherType = neighborhoodFragment.getOtherType();
            if (otherType != null) {
                if ((otherType.trim()).equals("")) {
                    otherType = "Other";
                }
                alertType = alertType.replace("Other", otherType);
            }
            String description = neighborhoodFragment.getDescription();
            List<Route> routesAffected = neighborhoodFragment.getRoutesAffected();

            if (neighborhood == null || alertType == null || description == null || description.equals("")) {
                // instruct the user that some parameter is missing information
                Toast toast = Toast.makeText(this, "Please enter any missing parameters.",
                        Toast.LENGTH_SHORT);
                toast.show();
            } else {
                // submit new alert
                submitButton.setEnabled(false);
                WMBController controller = WMBController.getInstance();
                // TODO: change to method that will post the alert with routes affected
                List<String> affectedRouteIds = new ArrayList<>();
                for (Route route : routesAffected) {
                    affectedRouteIds.add(route.getId());
                }

                controller.postAlert(neighborhood.getID(), alertType, description, "[User ID]", affectedRouteIds, new Callback<NeighborhoodAlert>() {
                    @Override
                    public void onResponse(Response<NeighborhoodAlert> response, Retrofit retrofit) {
                        // switch back to previous screen
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        finish();
                    }
                });
            }
        }
    }

    /**
     * Redirects the user to the previous screen
     *
     * @param view the button clicked
     */
    public void switchToPreviousScreen(View view) {
        finish();
    }

    /**
     * Opens the dialog for selecting routes affected
     *
     * @param view the current view
     */
    public void openRouteDialog(View view) {
        neighborhoodFragment.openRouteDialog(view);
    }

    /**
     * Implements the OnFragmentInteractionListener interfaces for BusRouteAlertFragment and
     * NeighborhoodAlertFragment.
     *
     * @param uri
     */
    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
