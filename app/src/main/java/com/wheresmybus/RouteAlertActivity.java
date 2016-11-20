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

import adapters.CommentAdapter;
import modules.Comment;
import modules.RouteAlert;
import modules.UserDataManager;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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

        // TODO: refactor this code so that it's not copied in the same place in 5 different classes
        // determine if the user has already downVoted/upVoted the alert
        UserDataManager userDataManager = UserDataManager.getManager();
        boolean alertIsUpVoted = userDataManager.getUpVotedAlertsByID().contains(alert.getId());
        boolean alertIsDownVoted = userDataManager.getDownVotedAlertsByID().contains(alert.getId());


        thumbsUp.setOnClickListener(new ThumbsUpListener(alert, alertIsUpVoted, numThumbsUp));
        //numThumbsUp.setText(alert.getUpvotes());        // TODO: fix this method call
        thumbsDown.setOnClickListener(new ThumbsDownListener(alert, alertIsDownVoted, numThumbsDown));
        //numThumbsDown.setText(alert.getDownvotes());

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
