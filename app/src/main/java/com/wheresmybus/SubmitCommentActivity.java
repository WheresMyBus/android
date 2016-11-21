package com.wheresmybus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import controllers.WMBController;
import modules.Alert;
import modules.Comment;
import modules.NeighborhoodAlert;
import modules.RouteAlert;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SubmitCommentActivity extends AppCompatActivity {
    private boolean isRouteAlert;
    private RouteAlert routeAlert;
    private NeighborhoodAlert neighborhoodAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Alert alert = (Alert) intent.getSerializableExtra("ALERT");
        if (alert instanceof RouteAlert) {
            isRouteAlert = true;
            routeAlert = (RouteAlert) alert;
        } else {
            isRouteAlert = false;
            neighborhoodAlert = (NeighborhoodAlert) alert;
        }
    }

    public void switchToPreviousScreen(View view) {
        finish();
    }

    public void switchToForum(View view) {
        EditText comment = (EditText) findViewById(R.id.comment_description);
        String description = comment.getText().toString();
        if (description == null || description.equals("")) {
            // instruct the user to enter a comment
            Toast toast = Toast.makeText(this, "Please enter a comment.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            // submit the user's comment
            WMBController controller = WMBController.getInstance();
            if (isRouteAlert) {
                // post route alert comment
                controller.postRouteAlertComment(routeAlert.getId(), description, "userid",
                        new Callback<Comment>() {
                    @Override
                    public void onResponse(Response<Comment> response, Retrofit retrofit) {

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            } else {
                // post neighborhood alert comment
                controller.postNeighborhoodAlertComment(neighborhoodAlert.getId(), description, "userid",
                        new Callback<Comment>() {
                    @Override
                    public void onResponse(Response<Comment> response, Retrofit retrofit) {

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }

            // switch back to the previous screen
            finish();
        }
    }
}
