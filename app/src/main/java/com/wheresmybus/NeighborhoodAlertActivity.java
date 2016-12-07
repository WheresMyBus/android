package com.wheresmybus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import adapters.CommentAdapter;
import modules.Comment;
import modules.Neighborhood;
import modules.NeighborhoodAlert;
import modules.Route;
import modules.UserDataManager;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by lesli_000 on 11/15/2016.
 */

/**
 * The activity for the screen where a neighborhood alert selected in a forum is displayed along
 * with the comments submitted for the alert and a button to submit a new comment.
 */
public class NeighborhoodAlertActivity extends AppCompatActivity {
    private NeighborhoodAlert alert;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    /**
     * Part of the call structure to display the activity.
     *
     * @param savedInstanceState previously saved state, or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighborhood_alert);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get the alert
        Intent intent = getIntent();
        alert = (NeighborhoodAlert) intent.getSerializableExtra("ALERT");

        // set page title
        setTitle(intent.getStringExtra("TITLE"));

        // set up the alert at the top of the page
        loadAlertData();

        // load comment data and set up list view for comments
        try {
            commentRequest();
        } catch (Exception e) {
            e.printStackTrace();
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
        if (id == R.id.action_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            return true;
        } else if (id == R.id.action_help) {
            Uri uri = Uri.parse("https://github.com/WheresMyBus/android/wiki/User-Documentation");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Refreshes the comments when the activity starts
     */
    @Override
    public void onStart() {
        super.onStart();
        refreshComments();
    }

    /**
     * Refreshes the comments when the activity restarts
     */
    @Override
    public void onRestart() {
        super.onRestart();
        refreshComments();
    }

    /**
     * Refreshes the comments when the activity resumes
     */
    @Override
    public void onResume() {
        super.onResume();
        refreshComments();
    }

    /**
     * Refresh the comments by getting the latest from the database
     */
    private void refreshComments() {
        try {
            commentRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: saving user data writes all of the user data to files, which could be unnecessarily
    // expensive to perform as often as we will be doing - Nick B. (though the files are small
    // enough that it probably won't matter
    /**
     * Saves any information relevant to the user, namely if the user upvoted or downvoted the
     * alert or any comments.
     */
    @Override
    public void onStop() {
        super.onStop();
        UserDataManager.getManager().saveUserData(this);
    }

    /**
     * Sets up the text at the top of the screen to display the information of the neighborhood
     * alert, including the alert types, the description, the routes possibly affected by the alert,
     * the date and time the alert was submitted, and the number of upvotes or downvotes the alert
     * has received. Also sets the listeners for the thumbs up and down buttons.
     */
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
        } else if (routesSet.size() > 10) {
            routes += " several";
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

        TimeZone zone = TimeZone.getDefault();

        dateFormatter = new SimpleDateFormat("E, MMM d");
        dateFormatter.setTimeZone(zone);

        timeFormatter = new SimpleDateFormat("h:mm a");
        timeFormatter.setTimeZone(zone);

        Date alertDate = alert.getDate();

        date.setText(dateFormatter.format(alertDate));
        time.setText(timeFormatter.format(alertDate));

        // determine if the user has already downVoted/upVoted the alert
        UserDataManager userDataManager = UserDataManager.getManager();
        boolean alertIsUpVoted = userDataManager.getUpVotedAlertsByID().contains(alert.getId());
        boolean alertIsDownVoted = userDataManager.getDownVotedAlertsByID().contains(alert.getId());

        thumbsUp.setOnClickListener(new ThumbsUpListener(alert, alertIsUpVoted, numThumbsUp, thumbsDown, numThumbsDown));
        numThumbsUp.setText(String.format(Locale.getDefault(), "%1$d", alert.getUpvotes()));        // TODO: fix this method call
        thumbsDown.setOnClickListener(new ThumbsDownListener(alert, alertIsDownVoted, numThumbsDown, thumbsUp, numThumbsUp));
        numThumbsDown.setText(String.format(Locale.getDefault(), "%1$d", alert.getDownvotes()));

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

    /**
     * Retrieves the comments submitted for the neighborhood alert and loads them on the page.
     *
     * @throws Exception if request fails
     */
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

    /**
     * Loads the list of comments submitted for the neighborhood alert onto the page displayed to
     * the user.
     *
     * @param comments the list of comments
     */
    private void loadComments(List<Comment> comments) {
        CommentAdapter adapter = new CommentAdapter(this, android.R.layout.simple_list_item_1,
                comments);
        ListView commentList = (ListView) findViewById(R.id.comments);
        commentList.setAdapter(adapter);
    }

    /**
     * Sends the user to the page where a new comment can be submitted for the neighborhood alert.
     *
     * @param view the button clicked
     */
    public void switchToSubmitComment(View view) {
        Intent intent = new Intent(this, SubmitCommentActivity.class);
        intent.putExtra("ALERT", alert);
        startActivity(intent);
    }
}
