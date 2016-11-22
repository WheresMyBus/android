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

    // need to add case where if user clicks button again it undoes the action from before (i.e.
    // removes an upvote and unfills the button

    /**
     * see @super
     * If neither this button or the corresponding thumbs down is pressed, highlights this button
     * and sends an upvote to the server. If this button is pressed, unhighlights the button and
     * sends an unvote to the server.
     * @param v see @super
     */
    @Override
    public void onClick(View v) {
        Set<Integer> upVotedSetByID; // the set of up-voted alerts or comments by ID
        UserDataManager userDataManager = UserDataManager.getManager();
        Integer ID; // the ID of this alert/comment
        boolean downvoted;

        if (isAlert) {
            downvoted = userDataManager.getDownVotedAlertsByID().contains(alert.getId());
            if (toggledOn) {
                userDataManager.getUpVotedAlertsByID().remove(alert.getId());
                alert.unvote(userDataManager.getUserID(), new ThumbsUpCallback(true));
            } else if (!downvoted) {
                userDataManager.getUpVotedAlertsByID().add(alert.getId());
                alert.upvote(userDataManager.getUserID(), new ThumbsUpCallback(true));
            }
            // send an upvote to the alert
            // alert.upvote();

            // change number of thumbs up shown
            //TextView numThumbsUp = (TextView) v.getRootView().findViewById(R.id.num_thumbs_up);
            //numThumbsUp.setText(alert.getUpvotes() + "");
            upVotedSetByID = userDataManager.getUpVotedAlertsByID();
            ID = alert.getId();
        } else {
            downvoted = userDataManager.getDownVotedCommentsByID().contains(comment.getId());
            // send an upvote to the comment
            // comment.upvote();
            if (toggledOn) {
                userDataManager.getUpVotedCommentsByID().remove(comment.getId());
                comment.unvote(userDataManager.getUserID(), new ThumbsUpCallback(false));
            } else if (!downvoted) {
                userDataManager.getUpVotedCommentsByID().add(comment.getId());
                comment.upvote(userDataManager.getUserID(), new ThumbsUpCallback(false));
            }
            // change number of thumbs up shown
            //TextView numThumbsUp = (TextView) v.getRootView().findViewById(R.id.num_thumbs_up);
            // numThumbsUp.setText(comment.getUpvotes() + "");
            upVotedSetByID = userDataManager.getUpVotedCommentsByID();
            ID = comment.getId();
        }

        // change the color of the button
        // change the color of the button
        ImageButton thumbsUp = (ImageButton) v.findViewById(R.id.thumbs_up);
        if (toggledOn) {
            thumbsUp.clearColorFilter();
            toggledOn = false;
        } else if (!downvoted){
            thumbsUp.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.green));
            toggledOn = true;
        }

        // to get the user ID: Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
    }
    private class ThumbsUpCallback implements Callback<VoteConfirmation> {
        private boolean useAlert;
        private ThumbsUpCallback(boolean useAlert) {
            this.useAlert = useAlert;
        }

        @Override
        public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
            VoteConfirmation confirmation = response.body();
            numThumbsUp.setText(confirmation.upvotes + "");
//            if (useAlert) {
//                //numThumbsUp.setText(alert.getUpvotes() + "");
//            } else {
//                //numThumbsUp.setText(comment.getUpvotes() + "");
//            }
        }
        @Override
        public void onFailure(Throwable t) {
        }
    }
}
