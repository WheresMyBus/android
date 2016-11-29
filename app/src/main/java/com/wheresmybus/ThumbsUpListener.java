package com.wheresmybus;

import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;
import java.util.Set;

import modules.Alert;
import modules.Comment;
import modules.UserDataManager;
import modules.VoteConfirmation;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Listener class for the thumbs up buttons on alerts and comments
 * on click, the listener changes the color of the button and sends
 * an upvote to the backend server accordingly. A button
 * will be unresponsive if the corresponding thumbs down button has been
 * pressed but not unpressed by the user.
 *
 * Created by lesli_000 on 11/12/2016.
 */
public class ThumbsUpListener implements View.OnClickListener {
    private Alert alert;
    private Comment comment;
    private boolean isAlert;
    private boolean toggledOn;
    private TextView numThumbsUp;

    /**
     * Constructs a new ThumbsUpListener for the given alert.
     * @param alert the alert to upvote
     * @param startsToggledOn true if the button should start toggled
     *                      for when we must remember that the user
     *                      had clicked this button before
     * @param numThumbsUp The text view holding the number of upvotes
     *                      to be displayed to the user and updated on click
     */
    public ThumbsUpListener(Alert alert, boolean startsToggledOn, TextView numThumbsUp) {
        this.alert = alert;
        isAlert = true;
        toggledOn = startsToggledOn;
        this.numThumbsUp = numThumbsUp;
    }

    /**
     * Constructs a new ThumbsUpListener for the given comment.
     * @param comment the comment to upvote
     * @param startsToggledOn true if the button should start toggled
     *                      for when we must remember that the user
     *                      had clicked this button before
     * @param numThumbsUp The text view holding the number of upvotes
     *                      to be displayed to the user and updated on click
     */
    public ThumbsUpListener(Comment comment, boolean startsToggledOn, TextView numThumbsUp) {
        this.comment = comment;
        isAlert = false;
        toggledOn = startsToggledOn;
        this.numThumbsUp = numThumbsUp;
    }

    /**
     * see @super
     * If neither this button or the corresponding thumbs down is pressed, highlights this button
     * and sends an upvote to the server. If this button is pressed, unhighlights the button and
     * sends an unvote to the server.
     * @param v see @super
     */
    @Override
    public void onClick(View v) {
        UserDataManager userDataManager = UserDataManager.getManager();
        boolean downvoted;

        if (isAlert) {
            downvoted = userDataManager.getDownVotedAlertsByID().contains(alert.getId());
            if (toggledOn) {
                // user has already liked the associated post, un-vote
                // and remove from the upvoted set
                userDataManager.getUpVotedAlertsByID().remove(alert.getId());
                alert.unvote(userDataManager.getUserID(), new ThumbsUpCallback());
            } else if (!downvoted) {
                // user has not liked or disliked this post, upvote and
                // add to the upvoted set
                userDataManager.getUpVotedAlertsByID().add(alert.getId());
                alert.upvote(userDataManager.getUserID(), new ThumbsUpCallback());
            }
        } else {
            downvoted = userDataManager.getDownVotedCommentsByID().contains(comment.getId());
            if (toggledOn) {
                userDataManager.getUpVotedCommentsByID().remove(comment.getId());
                comment.unvote(userDataManager.getUserID(), new ThumbsUpCallback());
            } else if (!downvoted) {
                userDataManager.getUpVotedCommentsByID().add(comment.getId());
                comment.upvote(userDataManager.getUserID(), new ThumbsUpCallback());
            }
        }
        // change the color of the button and change the toggled status
        ImageButton thumbsUp = (ImageButton) v.findViewById(R.id.thumbs_up);
        if (toggledOn) {
            thumbsUp.clearColorFilter();
            toggledOn = false;
        } else if (!downvoted){
            thumbsUp.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.green));
            toggledOn = true;
        }
    }

    // Helper class implementing Callback to construct callbacks in several places,
    // simply updates the number of likes displayed as is returned with the server response
    // to an upvote
    private class ThumbsUpCallback implements Callback<VoteConfirmation> {
        private ThumbsUpCallback() {
        }

        @Override
        public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
            VoteConfirmation confirmation = response.body();
            // use the upvotes returned from the response to update the thumbs up number next
            // to the button
            numThumbsUp.setText(String.format(Locale.getDefault(), "%1$d", confirmation.upvotes));
        }
        @Override
        public void onFailure(Throwable t) {
        }
    }
}
