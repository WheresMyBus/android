package com.wheresmybus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by lesli_000 on 11/8/2016.
 */

public class SubmitAlertActivity extends AppCompatActivity {
    private Button submitButton;
    private Spinner busRouteSpinner;
    private Spinner neighborhoodSpinner;
    private TextView busRoute;
    private TextView neighborhood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_alert);

        busRoute = (TextView) findViewById(R.id.choose_bus_route);
        busRoute.setVisibility(View.INVISIBLE);

        busRouteSpinner = (Spinner) findViewById(R.id.bus_route_spinner);
        busRouteSpinner.setVisibility(View.INVISIBLE);

        // neighborhood = (TextView) findViewById(R.id.choose_neighborhood);
        //neighborhood.setVisibility(View.INVISIBLE);

        // neighborhoodSpinner = (Spinner) findViewById(R.id.neighborhood_spinner);
        //neighborhoodSpinner.setVisibility(View.INVISIBLE);

        // add these things for selecting tag for alert and text box for alert description

        submitButton = (Button) findViewById(R.id.button_submit);
        submitButton.setVisibility(View.INVISIBLE);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_bus_route:
                if (checked)
                    // Pirates are the best

                    busRoute.setVisibility(View.VISIBLE);
                    busRouteSpinner.setVisibility(View.VISIBLE);
                    break;
            case R.id.radio_neighborhood:
                if (checked)
                    // Ninjas rule

                    busRoute.setVisibility(View.INVISIBLE);
                    busRouteSpinner.setVisibility(View.INVISIBLE);
                    submitButton.setVisibility(View.VISIBLE);
                    break;
        }
    }

    private void loadSpinnerData() {
        // get the names of bus routes or neighborhoods depending on which radio button selected
        // make sure data alphabetized
    }
}
