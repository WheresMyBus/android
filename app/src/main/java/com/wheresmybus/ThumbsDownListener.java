package com.wheresmybus;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Set;

import controllers.OBAController;
import controllers.WMBController;
import modules.Alert;
import modules.Comment;
import modules.UserDataManager;
import modules.VoteConfirmation;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by lesli_000 on 11/12/2016.
 */

public class ThumbsDownListener implements View.OnClickListener {
    private Alert alert;
    private Comment comment;
    private boolean isAlert;
    private boolean toggledOn;
    private TextView numThumbsDown;

    /**
     * TODO: fully document this constructor
     * @param alert
     * @param startsToggled true if the button should start toggled
     *                      for when we must remember that the user
     *                      had clicked this button before
     */
    public ThumbsDownListener(Alert alert, boolean startsToggled, TextView numThumbsDown) {
        // Note: the caller, which should be an adapter which also
        // initializes the button, must still set the color of the button
        // when startsToggled is true, this constructor does
        // not have access to the button that needs coloring. The
        // same is true for all thumbs-listener-type constructors.
        // - Nick B.
        this.alert = alert;
        isAlert = true;
        toggledOn = startsToggled;
        this.numThumbsDown = numThumbsDown;
    }

    /**
     * TODO: fully document this constructor
     * @param comment
     * @param startsToggledOn true if the button should start toggled
     *                      for when we must remember that the user
     *                      had clicked this button before
     */
    public ThumbsDownListener(Comment comment, boolean startsToggledOn, TextView numThumbsDown) {
        this.comment = comment;
        isAlert = false;
        toggledOn = startsToggledOn;
        this.numThumbsDown = numThumbsDown;
    }

    // need to add case where if user clicks button again it undoes the action from before (i.e.
    // removes a downvote and unfills the button
    @Override
    public void onClick(View v) {
        Set<Integer> downVotedSetByID; // the set of down-voted alerts or comments by ID
        UserDataManager userDataManager = UserDataManager.getManager();
        Integer ID; // the ID of this alert/comment

        if (isAlert) {
            // send a downvote to the controller
            // change number of thumbs down shown
            if (toggledOn) {
                // alert.undoDownVote();
            } else {
                alert.downvote(userDataManager.userID, new Callback<VoteConfirmation>() {
                    @Override
                    public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
                        numThumbsDown.setText(alert.getDownvotes() + "");
                    }
                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
            }
            //TextView numThumbsDown = (TextView) v.getRootView().findViewById(R.id.num_thumbs_down);
            numThumbsDown.setText(alert.getDownvotes() + "");
            // get the down-voted set for
            downVotedSetByID = userDataManager.getDownVotedAlertsByID();
            ID = alert.getId();
        } else {
            // send a downvote to the comment
            // comment.downvote();
            if (toggledOn) {
                // comment.undoDownVote();
            } else {
                comment.downvote(userDataManager.userID, new Callback<VoteConfirmation>() {
                    @Override
                    public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
                        numThumbsDown.setText(comment.getDownvotes() + "");
                    }
                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
            }

            // change number of thumbs down shown
            //TextView numThumbsDown = (TextView) v.getRootView().findViewById(R.id.num_thumbs_down);
            numThumbsDown.setText(comment.getDownvotes() + "");
            downVotedSetByID = userDataManager.getDownVotedCommentsByID();
            ID = comment.getId();
        }
        // change the color of the button
        ImageButton thumbsDown = (ImageButton) v.findViewById(R.id.thumbs_down);
        if (toggledOn) {
            thumbsDown.clearColorFilter();
            downVotedSetByID.remove(ID);
        } else {
            thumbsDown.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.orange));
            downVotedSetByID.add(ID);
        }
        toggledOn = !toggledOn;
        // to get the user ID: Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
    }
}
