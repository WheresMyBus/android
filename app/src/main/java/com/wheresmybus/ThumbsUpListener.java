package com.wheresmybus;

import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import modules.Alert;
import modules.Comment;

/**
 * Created by lesli_000 on 11/12/2016.
 */

public class ThumbsUpListener implements View.OnClickListener {
    private Alert alert;
    private Comment comment;
    private boolean isAlert;

    public ThumbsUpListener(Alert alert) {
        this.alert = alert;
        isAlert = true;
    }

    public ThumbsUpListener(Comment comment) {
        this.comment = comment;
        isAlert = false;
    }

    // need to add case where if user clicks button again it undoes the action from before (i.e.
    // removes an upvote and unfills the button
    @Override
    public void onClick(View v) {
        if (isAlert) {
            // send an upvote to the alert
            alert.upvote();

            // change number of thumbs up shown
            TextView numThumbsUp = (TextView) v.getRootView().findViewById(R.id.num_thumbs_up);
            numThumbsUp.setText(alert.getUpvotes() + "");
        } else {
            // send an upvote to the comment
            comment.upvote();

            // change number of thumbs up shown
            TextView numThumbsUp = (TextView) v.getRootView().findViewById(R.id.num_thumbs_up);
            numThumbsUp.setText(comment.getUpvotes() + "");
        }

        // change the color of the button
        ImageButton thumbsUp = (ImageButton) v.findViewById(R.id.thumbs_up);
        thumbsUp.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.green));

        // to get the user ID: Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
    }
}
