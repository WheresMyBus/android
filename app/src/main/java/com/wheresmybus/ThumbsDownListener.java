package com.wheresmybus;

import android.view.View;
import android.widget.TextView;

import modules.Alert;

/**
 * Created by lesli_000 on 11/12/2016.
 */

public class ThumbsDownListener implements View.OnClickListener {
    private Alert alert;

    public ThumbsDownListener(Alert alert) {
        this.alert = alert;
    }

    // need to add case where if user clicks button again it undoes the action from before (i.e.
    // removes a downvote and unfills the button
    @Override
    public void onClick(View v) {
        // send a downvote to the alert
        alert.downvote();
        // change color of button somehow (darker, brighter, slightly bigger)
        // change number of thumbs down shown
        TextView numThumbsDown = (TextView) v.getRootView().findViewById(R.id.num_thumbs_down);
        numThumbsDown.setText(alert.getDownvotes() + "");

        // to get the user ID: Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
    }
}
