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

    /**
     * Constructs a new ThumbsDownListener for the given alert.
     * @param alert the alert to downvote
     * @param startsToggled true if the button should start toggled
     *                      for when we must remember that the user
     *                      had clicked this button before
     * @param numThumbsDown The text view holding the number of thumbs down
     *                      to be displayed to the user and updated on click
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
     * Constructs a new ThumbsDownListener for the given comment.
     * @param comment the comment to downvote
     * @param startsToggledOn true if the button should start toggled
     *                      for when we must remember that the user
     *                      had clicked this button before
     * @param numThumbsDown The text view holding the number of thumbs down
     *                      to be displayed to the user and updated on click
     */
    public ThumbsDownListener(Comment comment, boolean startsToggledOn, TextView numThumbsDown) {
        this.comment = comment;
        isAlert = false;
        toggledOn = startsToggledOn;
        this.numThumbsDown = numThumbsDown;
    }

    // need to add case where if user clicks button again it undoes the action from before (i.e.
    // removes a downvote and unfills the button

    /**
     * see @super
     * If neither this button or the corresponding thumbs up is pressed, highlights this button
     * and sends a downvote to the server. If this button is pressed, unhighlights this button
     * and sends an unvote to the server
     * @param v see @super
     */
    @Override
    public void onClick(View v) {
        Set<Integer> downVotedSetByID; // the set of down-voted alerts or comments by ID
        UserDataManager userDataManager = UserDataManager.getManager();
        Integer ID; // the ID of this alert/comment
        boolean upvoted;

        if (isAlert) {
            upvoted = userDataManager.getUpVotedAlertsByID().contains(alert.getId());
            // send a downvote to the controller
            // change number of thumbs down shown
            if (toggledOn) {
                userDataManager.getDownVotedAlertsByID().remove(alert.getId());
                alert.unvote(userDataManager.getUserID(), new ThumbsDownCallback(true));
            } else if (!upvoted) {
                userDataManager.getDownVotedAlertsByID().add(alert.getId());
                alert.downvote(userDataManager.getUserID(), new ThumbsDownCallback(true));
            }
            //TextView numThumbsDown = (TextView) v.getRootView().findViewById(R.id.num_thumbs_down);
            //numThumbsDown.setText(alert.getDownvotes() + "");
            // get the down-voted set for
            downVotedSetByID = userDataManager.getDownVotedAlertsByID();
            ID = alert.getId();
        } else {
            upvoted = userDataManager.getUpVotedCommentsByID().contains(comment.getId());
            // send a downvote to the comment
            // comment.downvote();
            if (toggledOn) {
                userDataManager.getDownVotedCommentsByID().remove(comment.getId());
                comment.unvote(userDataManager.getUserID(), new ThumbsDownCallback(false));
            } else if (!upvoted) {
                userDataManager.getDownVotedCommentsByID().add(comment.getId());
                comment.downvote(userDataManager.getUserID(), new ThumbsDownCallback(false));
            }

            // change number of thumbs down shown
            //TextView numThumbsDown = (TextView) v.getRootView().findViewById(R.id.num_thumbs_down);
            //numThumbsDown.setText(comment.getDownvotes() + "");
            downVotedSetByID = userDataManager.getDownVotedCommentsByID();
            ID = comment.getId();
        }
        // change the color of the button
        ImageButton thumbsDown = (ImageButton) v.findViewById(R.id.thumbs_down);
        if (toggledOn) {
            thumbsDown.clearColorFilter();
            toggledOn = false;
        } else if (!upvoted){
            thumbsDown.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.orange));
            toggledOn = true;
        }
        // to get the user ID: Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
    }
    private class ThumbsDownCallback implements Callback<VoteConfirmation> {
        private boolean useAlert;
        private ThumbsDownCallback(boolean useAlert) {
            this.useAlert = useAlert;
        }

        @Override
        public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
            VoteConfirmation confirmation = response.body();
            numThumbsDown.setText(confirmation.downvotes + "");
//            if (useAlert) {
//                numThumbsDown.setText(alert.getUpvotes() + "");
//            } else {
//                numThumbsDown.setText(comment.getUpvotes() + "");
//            }
        }
        @Override
        public void onFailure(Throwable t) {
        }
    }
}
