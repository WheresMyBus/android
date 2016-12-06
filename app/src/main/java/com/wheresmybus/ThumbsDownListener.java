package com.wheresmybus;

import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

import modules.Alert;
import modules.Comment;
import modules.UserDataManager;
import modules.VoteConfirmation;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Listener class for the thumbs down buttons on alerts and comments
 * on click, the listener changes the color of the button and sends
 * a downvote to the backend server accordingly. A button
 * will be unresponsive if the corresponding thumbs up button has been
 * pressed but not unpressed by the user.
 *
 * Created by lesli_000 on 11/12/2016.
 */
public class ThumbsDownListener implements View.OnClickListener {
    private Alert alert;
    private Comment comment;
    private boolean isAlert;
    private boolean toggledOn;
    private TextView numThumbsDown;
    private ImageButton thumbsUpButton;
    private TextView numThumbsUp;

    /**
     * Constructs a new ThumbsDownListener for the given alert.
     * @param alert the alert to downvote
     * @param startsToggled true if the button should start toggled
     *                      for when we must remember that the user
     *                      had clicked this button before
     * @param numThumbsDown The text view holding the number of thumbs down
     *                      to be displayed to the user and updated on click
     */
    public ThumbsDownListener(Alert alert, boolean startsToggled, TextView numThumbsDown, ImageButton thumbsUpButton, TextView numThumbsUp) {
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
        this.thumbsUpButton = thumbsUpButton;
        this.numThumbsUp = numThumbsUp;
    }

    /**
     * Constructs a new ThumbsDownListener for the given comment.
     * @param comment the comment to downvote
     * @param startsToggledOn true if the button should start toggled
     *                      for when we must remember that the user
     *                      had clicked this button before
     * @param numThumbsDown The text view holding the number of thumbs down
     *                      to be displayed to the user and updated on click
     */
    public ThumbsDownListener(Comment comment, boolean startsToggledOn, TextView numThumbsDown, ImageButton thumbsUpButton, TextView numThumbsUp) {
        this.comment = comment;
        isAlert = false;
        toggledOn = startsToggledOn;
        this.numThumbsDown = numThumbsDown;
        this.thumbsUpButton = thumbsUpButton;
        this.numThumbsUp = numThumbsUp;
    }

    /**
     * see @super
     * If neither this button or the corresponding thumbs up is pressed, highlights this button
     * and sends a downvote to the server. If this button is pressed, unhighlights this button
     * and sends an unvote to the server
     * @param v see @super
     */
    @Override
    public void onClick(View v) {
        final UserDataManager userDataManager = UserDataManager.getManager();
        boolean upvoted;
        final ImageButton finalThumbsUp = thumbsUpButton;
        final TextView finalNumThumbsUp = numThumbsUp;
        if (isAlert) {
            toggledOn = userDataManager.getDownVotedAlertsByID().contains(alert.getId());
            upvoted = userDataManager.getUpVotedAlertsByID().contains(alert.getId());
            if (toggledOn) {
                // user has already disliked the associated post, un-vote
                // and remove from the downvoted set
                userDataManager.getDownVotedAlertsByID().remove(alert.getId());
                alert.unvote(userDataManager.getUserID(), new ThumbsDownCallback());
            } else if (!upvoted) {
                // user has not liked or disliked this post, downvote and
                // add to the downvoted set
                userDataManager.getDownVotedAlertsByID().add(alert.getId());
                alert.downvote(userDataManager.getUserID(), new ThumbsDownCallback());
            } else {
                // user has liked this post, unvote then revote
                userDataManager.getUpVotedAlertsByID().remove(alert.getId());
                alert.unvote(userDataManager.getUserID(), new Callback<VoteConfirmation>() {
                    @Override
                    public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
                        // set the number of likes
                        finalNumThumbsUp.setText(
                                String.format(Locale.getDefault(), "%1$d", response.body().upvotes)
                        );
                        // clear the thumbs up color
                        finalThumbsUp.clearColorFilter();

                        // vote down the post
                        userDataManager.getDownVotedAlertsByID().add(alert.getId());
                        alert.downvote(userDataManager.getUserID(), new ThumbsDownCallback());
                    }
                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
            }
        } else {
            toggledOn = userDataManager.getDownVotedCommentsByID().contains(comment.getId());
            upvoted = userDataManager.getUpVotedCommentsByID().contains(comment.getId());
            if (toggledOn) {
                userDataManager.getDownVotedCommentsByID().remove(comment.getId());
                comment.unvote(userDataManager.getUserID(), new ThumbsDownCallback());
            } else if (!upvoted) {
                userDataManager.getDownVotedCommentsByID().add(comment.getId());
                comment.downvote(userDataManager.getUserID(), new ThumbsDownCallback());
            } else {
                // user has liked this post, unvote then revote
                userDataManager.getUpVotedCommentsByID().remove(comment.getId());
                comment.unvote(userDataManager.getUserID(), new Callback<VoteConfirmation>() {
                    @Override
                    public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
                        // set the number of likes
                        finalNumThumbsUp.setText(
                                String.format(Locale.getDefault(), "%1$d", response.body().upvotes)
                        );
                        // clear the thumbs up color
                        finalThumbsUp.clearColorFilter();

                        // vote down the post
                        userDataManager.getDownVotedCommentsByID().add(comment.getId());
                        comment.downvote(userDataManager.getUserID(), new ThumbsDownCallback());
                    }
                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
            }

        }
        // change the color of the button and change the toggled status
        ImageButton thumbsDown = (ImageButton) v.findViewById(R.id.thumbs_down);
        if (toggledOn) {
            thumbsDown.clearColorFilter();
            toggledOn = false;
        } else {
            thumbsDown.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.orange));
            toggledOn = true;
        }
    }

    // Helper class implementing Callback to construct callbacks in several places,
    // simply updates the number of dislikes displayed as is returned with the server response
    // to an downvote
    private class ThumbsDownCallback implements Callback<VoteConfirmation> {
        private ThumbsDownCallback() {
        }

        @Override
        public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
            VoteConfirmation confirmation = response.body();
            // use the downvotes returned from the response to update the thumbs down number next
            // to the button
            String dislikeNum = String.format(Locale.getDefault(), "%1$d", confirmation.downvotes);
            numThumbsDown.setText(dislikeNum);
        }
        @Override
        public void onFailure(Throwable t) {
        }
    }
}
