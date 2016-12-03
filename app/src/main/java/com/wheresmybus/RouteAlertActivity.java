package com.wheresmybus;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import modules.RouteAlert;
import modules.UserDataManager;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * The activity for the screen where a route alert selected in a forum is displayed along with the
 * comments submitted for the alert and a button to submit a new comment.
 */
public class RouteAlertActivity extends AppCompatActivity {
    private RouteAlert alert;
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

    /**
     * Saves any information relevant to the user, namely if the user upvoted or downvoted the
     * alert or any comments.
     */
    // TODO: saving user data writes all of the user data to files, which could be unnecessarily
    // expensive to perform as often as we will be doing - Nick B. (though the files are small
    // enough that it probably won't matter
    @Override
    public void onStop() {
        super.onStop();
        UserDataManager.getManager().saveUserData(this);
    }

    /**
     * Sets up the text at the top of the screen to display the information of the route alert,
     * including the alert types, the description, the date and time the alert was submitted, and
     * the number of upvotes or downvotes the alert has received. Also sets the listeners for the
     * thumbs up and down buttons.
     */
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

        // sets up formatters for the date and time the alert was submitted if not already set up
        TimeZone zone = TimeZone.getDefault();

        dateFormatter = new SimpleDateFormat("E, MMM d");
        dateFormatter.setTimeZone(zone);

        timeFormatter = new SimpleDateFormat("h:mm a");
        timeFormatter.setTimeZone(zone);

        Date alertDate = alert.getDate();

        date.setText(dateFormatter.format(alertDate));
        time.setText(timeFormatter.format(alertDate));

        // TODO: refactor this code so that it's not copied in the same place in 5 different classes
        // determine if the user has already downVoted/upVoted the alert
        UserDataManager userDataManager = UserDataManager.getManager();
        boolean alertIsUpVoted = userDataManager.getUpVotedAlertsByID().contains(alert.getId());
        boolean alertIsDownVoted = userDataManager.getDownVotedAlertsByID().contains(alert.getId());


        thumbsUp.setOnClickListener(new ThumbsUpListener(alert, alertIsUpVoted, numThumbsUp));
        numThumbsUp.setText(String.format(Locale.getDefault(), "%1$d", alert.getUpvotes()));
        thumbsDown.setOnClickListener(new ThumbsDownListener(alert, alertIsDownVoted, numThumbsDown));
        numThumbsDown.setText(String.format(Locale.getDefault(), "%1$d", alert.getDownvotes()));

        // TODO: refactor this code so that it's not copied in the same place in 5 different classes
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
     * Retrieves the comments submitted for the route alert and loads them on the page.
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
