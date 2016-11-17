package com.wheresmybus;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import modules.NeighborhoodAlert;
import modules.Route;

/**
 * Created by lesli_000 on 11/15/2016.
 */

public class NeighborhoodAlertActivity extends AppCompatActivity {
    private NeighborhoodAlert alert;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighborhood_alert);

        // get the alert
        Intent intent = getIntent();
        alert = (NeighborhoodAlert) intent.getSerializableExtra("ALERT");

        // set up the alert at the top of the page
        loadAlertData();

        // get data from the controller
        // set up alert at top of page
        // load data into list view
    }

    private void loadAlertData() {
        // get references to the different view we want to change
        TextView alertType = (TextView) findViewById(R.id.alert_type);
        TextView alertDescription = (TextView) findViewById(R.id.alert_description);
        TextView routesAffected = (TextView) findViewById(R.id.routes_affected);
        TextView date = (TextView) findViewById(R.id.date);
        TextView time = (TextView) findViewById(R.id.time);
        ImageButton thumbsUp = (ImageButton) findViewById(R.id.thumbs_up);
        TextView numThumbsUp = (TextView) findViewById(R.id.num_thumbs_up);
        ImageButton thumbsDown = (ImageButton) findViewById(R.id.thumbs_down);
        TextView numThumbsDown = (TextView) findViewById(R.id.num_thumbs_down);

        // fill the views with data
        alertType.setText(alert.getType());
        alertDescription.setText(alert.getDescription());

        // gets the header displayed to the user for routes affected
        String routes = routesAffected.getText().toString();

        // gets the set of routes affected from the alert
        List<Route> routesSet = alert.getRoutesAffected();
        if (routesSet.isEmpty()) {
            // reports no routes affected
            routes += " none";
        } else {
            // adds the numbers of the routes affected to the string that will be displayed
            boolean firstRoute = true;
            for (Route route : routesSet) {
                if (firstRoute) {
                    // special formatting case for the first route number added
                    routes += " " + route.getNumber();
                    firstRoute = false;
                } else {
                    routes += ", " + route.getNumber();
                }
            }
        }

        routesAffected.setText(routes);

        if (dateFormatter == null) {
            dateFormatter = new SimpleDateFormat("E, MMM d");
        }
        if (timeFormatter == null) {
            timeFormatter = new SimpleDateFormat("h:mm a");
        }

        Date alertDate = alert.getDate();

        date.setText(dateFormatter.format(alertDate));
        time.setText(timeFormatter.format(alertDate));

        thumbsUp.setOnClickListener(new ThumbsUpListener(alert));
        //numThumbsUp.setText(alert.getUpvotes());        // TODO: fix this method call
        thumbsDown.setOnClickListener(new ThumbsDownListener(alert));
        //numThumbsDown.setText(alert.getDownvotes());
    }
}
