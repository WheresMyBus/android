package com.wheresmybus;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import modules.Alert;
import modules.Comment;

/**
 * Created by lesli_000 on 11/12/2016.
 */

public class ThumbsDownListener implements View.OnClickListener {
    private Alert alert;
    private Comment comment;
    private boolean isAlert;

    public ThumbsDownListener(Alert alert) {
        this.alert = alert;
        isAlert = true;
    }

    public ThumbsDownListener(Comment comment) {
        this.comment = comment;
        isAlert = false;
    }

    // need to add case where if user clicks button again it undoes the action from before (i.e.
    // removes a downvote and unfills the button
    @Override
    public void onClick(View v) {
        if (isAlert) {
            // send a downvote to the alert
            alert.downvote();

            // change number of thumbs down shown
            TextView numThumbsDown = (TextView) v.getRootView().findViewById(R.id.num_thumbs_down);
            numThumbsDown.setText(alert.getDownvotes() + "");
        } else {
            // send a downvote to the comment
            comment.downvote();

            // change number of thumbs down shown
            TextView numThumbsDown = (TextView) v.getRootView().findViewById(R.id.num_thumbs_down);
            numThumbsDown.setText(comment.getDownvotes() + "");
        }

        // change the color of the button
        ImageButton thumbsDown = (ImageButton) v.findViewById(R.id.thumbs_down);
        thumbsDown.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.orange));

        // to get the user ID: Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
    }
}
