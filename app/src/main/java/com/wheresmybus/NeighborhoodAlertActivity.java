package com.wheresmybus;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import adapters.CommentAdapter;
import modules.Comment;
import modules.NeighborhoodAlert;
import modules.Route;
import modules.UserDataManager;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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

        // load comment data and set up list view for comments
        try {
            commentRequest();
        } catch (Exception e) {
            e.printStackTrace();
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

        // determine if the user has already downVoted/upVoted the alert
        UserDataManager userDataManager = UserDataManager.getManager();
        boolean alertIsUpVoted = userDataManager.getUpVotedAlertsByID().contains(alert.getId());
        boolean alertIsDownVoted = userDataManager.getDownVotedAlertsByID().contains(alert.getId());

        thumbsUp.setOnClickListener(new ThumbsUpListener(alert, alertIsUpVoted, numThumbsUp));
        //numThumbsUp.setText(alert.getUpvotes());        // TODO: fix this method call
        thumbsDown.setOnClickListener(new ThumbsDownListener(alert, alertIsDownVoted, numThumbsDown));
        //numThumbsDown.setText(alert.getDownvotes());

        // color the thumbsUp/thumbsDown buttons if this user has already clicked those buttons
        // in a previous session
        if (alertIsUpVoted) {
            int green = ContextCompat.getColor(this, R.color.green);
            thumbsUp.setColorFilter(green);
        }
        if (alertIsDownVoted) {
            int orange = ContextCompat.getColor(this, R.color.orange);
            thumbsDown.setColorFilter(orange);
        }
    }

    private void commentRequest() throws Exception {
        alert.getComments(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Response<List<Comment>> response, Retrofit retrofit) {
                List<Comment> comments = new ArrayList<Comment>(response.body());
                loadComments(comments);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void loadComments(List<Comment> comments) {
        CommentAdapter adapter = new CommentAdapter(this, android.R.layout.simple_list_item_1,
                comments);
        ListView commentList = (ListView) findViewById(R.id.comments);
        commentList.setAdapter(adapter);
    }

    public void switchToSubmitComment(View view) {
        Intent intent = new Intent(this, SubmitCommentActivity.class);
        intent.putExtra("ALERT", alert);
        startActivity(intent);
    }
}
