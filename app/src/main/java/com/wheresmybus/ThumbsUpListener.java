package com.wheresmybus;

import android.view.View;
import android.widget.TextView;

import modules.Alert;

/**
 * Created by lesli_000 on 11/12/2016.
 */

public class ThumbsUpListener implements View.OnClickListener {
    private Alert alert;

    public ThumbsUpListener(Alert alert) {
        this.alert = alert;
    }

    // need to add case where if user clicks button again it undoes the action from before (i.e.
    // removes an upvote and unfills the button
    @Override
    public void onClick(View v) {
        // send an upvote to the alert
        alert.upvote();
        // change color of button
        // change number of thumbs up shown
        TextView numThumbsUp = (TextView) v.findViewById(R.id.num_thumbs_up);
        numThumbsUp.setText(alert.getUpvotes());
    }
}
