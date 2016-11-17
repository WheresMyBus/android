package com.wheresmybus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import modules.RouteAlert;

public class RouteAlertActivity extends AppCompatActivity {
    private RouteAlert alert;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_alert);

        // get the alert
        Intent intent = getIntent();
        alert = (RouteAlert) intent.getSerializableExtra("ALERT");

        // set up the alert at the top of the page
        loadAlertData();

        // load comment data and set up list view for comments
        try {
            commentRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAlertData() {
        // get references to the different view we want to change
        TextView alertType = (TextView) findViewById(R.id.alert_type);
        TextView alertDescription = (TextView) findViewById(R.id.alert_description);
        TextView date = (TextView) findViewById(R.id.date);
        TextView time = (TextView) findViewById(R.id.time);
        ImageButton thumbsUp = (ImageButton) findViewById(R.id.thumbs_up);
        TextView numThumbsUp = (TextView) findViewById(R.id.num_thumbs_up);
        ImageButton thumbsDown = (ImageButton) findViewById(R.id.thumbs_down);
        TextView numThumbsDown = (TextView) findViewById(R.id.num_thumbs_down);

        // fill the views with data
        alertType.setText(alert.getType());
        alertDescription.setText(alert.getDescription());

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

    private void commentRequest() throws Exception {

    }

    public void switchToSubmitComment(View view) {
        Intent intent = new Intent(this, SubmitCommentActivity.class);
        intent.putExtra("ALERT", alert);
        startActivity(intent);
    }
}
