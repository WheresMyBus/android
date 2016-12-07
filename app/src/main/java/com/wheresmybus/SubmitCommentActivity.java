package com.wheresmybus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

    /**
     * Part of the call structure to display this activity.
     *
     * @param savedInstanceState previously saved state, or null
     */
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
     * Redirects the user to the previous screen
     *
     * @param view the button clicked
     */
    public void switchToPreviousScreen(View view) {
        finish();
    }

    /**
     * Redirects the user to the forum screen
     *
     * @param view the button clicked
     */
    public void switchToForum(View view) {
        EditText comment = (EditText) findViewById(R.id.comment_description);
        String description = comment.getText().toString();
        if (description.equals("")) {
            // instruct the user to enter a comment
            Toast toast = Toast.makeText(this, "Please enter a comment.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            // submit the user's comment
            view.setEnabled(false);
            WMBController controller = WMBController.getInstance();
            if (isRouteAlert) {
                // post route alert comment
                controller.postRouteAlertComment(routeAlert.getId(), description, "userid",
                        new Callback<Comment>() {
                    @Override
                    public void onResponse(Response<Comment> response, Retrofit retrofit) {
                        // switch back to previous screen
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        finish();
                    }
                });
            } else {
                // post neighborhood alert comment
                controller.postNeighborhoodAlertComment(neighborhoodAlert.getId(), description, "userid",
                        new Callback<Comment>() {
                    @Override
                    public void onResponse(Response<Comment> response, Retrofit retrofit) {
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
}
