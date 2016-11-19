package com.wheresmybus;

import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Set;

import modules.Alert;
import modules.Comment;
import modules.UserDataManager;

/**
 * Created by lesli_000 on 11/12/2016.
 */

public class ThumbsUpListener implements View.OnClickListener {
    private Alert alert;
    private Comment comment;
    private boolean isAlert;
    private boolean toggledOn;

    public ThumbsUpListener(Alert alert, boolean startsToggledOn) {
        this.alert = alert;
        isAlert = true;
        toggledOn = startsToggledOn;
    }

    public ThumbsUpListener(Comment comment, boolean startsToggledOn) {
        this.comment = comment;
        isAlert = false;
        toggledOn = startsToggledOn;
    }

    // need to add case where if user clicks button again it undoes the action from before (i.e.
    // removes an upvote and unfills the button
    @Override
    public void onClick(View v) {
        Set<Integer> upVotedSetByID; // the set of up-voted alerts or comments by ID
        UserDataManager userDataManager = UserDataManager.getManager();
        Integer ID; // the ID of this alert/comment
        if (isAlert) {
            // send an upvote to the alert
            // alert.upvote();

            // change number of thumbs up shown
            TextView numThumbsUp = (TextView) v.getRootView().findViewById(R.id.num_thumbs_up);
            numThumbsUp.setText(alert.getUpvotes() + "");
            upVotedSetByID = userDataManager.getUpVotedAlertsByID();
            ID = alert.getId();
        } else {
            // send an upvote to the comment
            // comment.upvote();

            // change number of thumbs up shown
            TextView numThumbsUp = (TextView) v.getRootView().findViewById(R.id.num_thumbs_up);
            numThumbsUp.setText(comment.getUpvotes() + "");
            upVotedSetByID = userDataManager.getUpVotedCommentsByID();
            ID = comment.getId();
        }

        // change the color of the button
        // change the color of the button
        ImageButton thumbsUp = (ImageButton) v.findViewById(R.id.thumbs_up);
        if (toggledOn) {
            thumbsUp.clearColorFilter();
            upVotedSetByID.remove(ID);
        } else {
            thumbsUp.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.green));
            upVotedSetByID.add(ID);
        }
        toggledOn = !toggledOn;

        // to get the user ID: Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
    }
}
