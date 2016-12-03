package modules;

import android.util.Pair;

import com.google.gson.annotations.SerializedName;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import controllers.WMBController;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by lidav on 10/25/2016.
 *
 * Stores data about a alert of a specific neighborhood
 * Invariant: id = -1 if id has not been set, else id > 0
 *          date, type != null
 *          either neighborhood != null, or neighborhoodID != -1
 *          routesAffected != null and all elements of routesAffected != null
 */

public class NeighborhoodAlert extends Alert {
    // This is the old stuff.
    private Neighborhood neighborhood;

    @SerializedName("routes_affected")
    private ArrayList<Route> routesAffected;

    @SerializedName("neighborhood_id")
    private int neighborhoodID;

    /**
     * Constructs a NeighborhoodAlert with id initialized to -1
     * and upvotes/downvotes initialized to 0
     * Used for creating a new NeighborhoodAlert to
     * post to the server, when the id is not defined yet.
     * @param neighborhood neighborhood of the alert
     * @param date Date the alert was posted
     * @param type type of alert posted
     * @param description the text describing the details of the alert
     * @param creatorID id of the creator of the post
     * @param routesAffected List of routes that are affected
     * @throws IllegalArgumentException if any of neighborhood, date, type,
     *              description, coordinates are null
     * @throws IllegalArgumentException if creatorID < 1 or upvotes, downvotes < 0
     */
    public NeighborhoodAlert(Neighborhood neighborhood, Date date, String description,
                             String type, String creatorID,
                             List<Route> routesAffected) {
        super(description, date, type, creatorID);
        if(neighborhood == null || routesAffected == null) {
            if(neighborhood == null) {
                throw new IllegalArgumentException("null neighborhood");
            } else {
                throw new IllegalArgumentException("null routesAffected list");
            }
        }
        this.neighborhood = neighborhood;
        this.neighborhoodID = -1; // unused for this constructor
        this.routesAffected = new ArrayList<>(routesAffected);
        checkRep();
    }

    /**
     * Creates a Neighborhood alert with upvotes, downvotes, and id as parameters
     * initializes routesAffected to empty List, and references a neighborhood by its id.
     * Used for building NeighborhoodAlerts from the data retrieved from the server, when
     * the id is known and some fields can be blank.
     * @param neighborhoodID neighborhood of the alert
     * @param date Date the alert was posted
     * @param alertType type of alert posted
     * @param description the text describing the details of the alert
     * @param user_id id of the creator of the post
     * @param upvotes upvotes to initialize the alert to
     * @param downvotes downvotes to initialize the alert to
     * @throws IllegalArgumentException if any of date, alertType, user_id
     *              description are null
     * @throws IllegalArgumentException if any of neighborhoodID, upvotes, downvotes < 0
     */
    public NeighborhoodAlert(int neighborhoodID, int alertID, String user_id, String alertType,
                             String description, Date date, int upvotes, int downvotes) {
        super(alertID, user_id, alertType, description, date, upvotes, downvotes);
        if(neighborhoodID < 0) {
            throw new IllegalArgumentException("neighborhoodID < 0");
        }
        this.neighborhoodID = neighborhoodID;
        routesAffected = new ArrayList<>();
        checkRep();
    }

    /**
     * Gets deep copy of Neighborhood this alert is related to
     * @return neighborhood of this alert
     */
    public Neighborhood getNeighborhood() {
        return new Neighborhood(neighborhood.getID(), neighborhood.getName());
    }

    /**
     * Adds new routes to the list of routes possibly affected by this neighborhood alert.
     * @param newRoute Route to add to affected routes
     * @return true if none of the elements of routesAffected were already stored in the list of
     *              routes affected by this alert, or false otherwise
     * @throws IllegalArgumentException if newRoute is null
     */
    public boolean addAffectedRoute(Route newRoute) {
        if(newRoute == null) {
            throw new IllegalArgumentException();
        }
        boolean b = routesAffected.add(newRoute);
        checkRep();
        return b;
    }

    /**
     * Returns the list of routes affected
     * @return List of routes affected
     */
    public List<Route> getRoutesAffected() {
        checkRep();
        return new ArrayList<>(routesAffected);
    }

    /**
     * Upvotes the neighborhood alert in the server
     * @param userID id of the user who is upvoting the alert
     * @param callback Callback that describes what to do when the server responds
     */
    @Override
    public void upvote(String userID, Callback<VoteConfirmation> callback) {
        final WMBController controller = WMBController.getInstance();
        final NeighborhoodAlert self = this;
        final Callback<VoteConfirmation> cb = callback;
        controller.neighborhoodAlertUpvote(this.getId(), userID, new Callback<VoteConfirmation>() {
            /**
             * On success, upvotes the Alert locally and invokes the passed callback's onResponse()
             * @param response response that was returned
             * @param retrofit Retrofit that the callback was queued on
             */
            @Override
            public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
                self.setVotes(response.body());
                cb.onResponse(response, retrofit);
            }

            /**
             * On failure, invokes the passed callback's onFailure() with the error
             * @param t error to be thrown
             */
            @Override
            public void onFailure(Throwable t) {
                cb.onFailure(t);
            }
        });
    }

    /**
     * Downvotes the neighborhood alert in the server
     * @param userID id of the user who is downvoting the alert
     * @param callback Callback that describes what to do when the server responds
     */
    @Override
    public void downvote(String userID, Callback<VoteConfirmation> callback) {
        final WMBController controller = WMBController.getInstance();
        final NeighborhoodAlert self = this;
        final Callback<VoteConfirmation> cb = callback;
        controller.neighborhoodAlertDownvote(this.getId(), userID, new Callback<VoteConfirmation>() {
            /**
             * On success, downvotes the Alert locally and invokes the passed callback's onResponse()
             * @param response response that was returned
             * @param retrofit Retrofit that the callback was queued on
             */
            @Override
            public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
                self.setVotes(response.body());
                cb.onResponse(response, retrofit);
            }
            /**
             * On failure, invokes the passed callback's onFailure() with the error
             * @param t error to be thrown
             */
            @Override
            public void onFailure(Throwable t) {
                cb.onFailure(t);
            }
        });
    }

    /**
     * Removes the vote this user posted previously for this alert
     * @param userID id of the user who is unvoting the alert
     * @param callback Callback that describes what to do when the server responds
     */
    @Override
    public void unvote(String userID, Callback<VoteConfirmation> callback) {
        final WMBController controller = WMBController.getInstance();
        final NeighborhoodAlert self = this;
        final Callback<VoteConfirmation> cb = callback;
        controller.neighborhoodAlertUnvote(this.getId(), userID, new Callback<VoteConfirmation>() {
            /**
             * On success, unvotes the Alert locally and invokes the passed callback's onResponse()
             * @param response response that was returned
             * @param retrofit Retrofit that the callback was queued on
             */
            @Override
            public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
                self.setVotes(response.body());
                cb.onResponse(response, retrofit);
            }
            /**
             * On failure, invokes the passed callback's onFailure() with the error
             * @param t error to be thrown
             */
            @Override
            public void onFailure(Throwable t) {
                cb.onFailure(t);
            }
        });
    }

    /**
     * gets the comments associated with this alert and handles them with the callback.
     * @param callback Callback that determines what to do when the server responds
     */
    @Override
    public void getComments(Callback<List<Comment>> callback) {
        WMBController controller = WMBController.getInstance();
        controller.getNeighborhoodAlertComments(this.getId(), callback);
    }

    @Override
    protected void checkRep() {
        super.checkRep();
        Assert.assertTrue(neighborhood != null || neighborhoodID != -1);
        Assert.assertTrue(routesAffected != null);
        Assert.assertFalse(routesAffected.contains(null));
    }
}
