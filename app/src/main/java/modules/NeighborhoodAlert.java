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
 *          neighborhood, date, type, coordinates != null
 *          routesAffected != null and all elements of routesAffected != null
 */

public class NeighborhoodAlert extends Alert {
    // This is the old stuff.
    private Neighborhood neighborhood;
    private List<Route> routesAffected;

    @SerializedName("neighborhood_id")
    private int neighborhoodID;

    /**
     * Constructs a NeighborhoodAlert with id initialized to -1
     * and upvotes/downvotes initialized to 0
     * @param neighborhood neighborhood of the alert
     * @param date Date the alert was posted
     * @param type type of alert posted
     * @param coordinates position of alert
     * @param description the text describing the details of the alert
     * @param creatorID id of the creator of the post
     * @throws IllegalArgumentException if any of neighborhood, date, type,
     *              description, coordinates are null
     * @throws IllegalArgumentException if creatorID < 1
     */
    public NeighborhoodAlert(Neighborhood neighborhood, Date date, String description,
                             String type, Pair<Double, Double> coordinates, int creatorID,
                             List<Route> routesAffected) {
        super(description, date, type, creatorID, coordinates);
        if(neighborhood == null || routesAffected == null) {
            throw new IllegalArgumentException("null parameters");
        }
        this.neighborhood = neighborhood;
        this.routesAffected = routesAffected;
    }

    /**
     * TODO: document this.
     * @param neighborhoodID
     * @param alertID
     * @param user_id
     * @param alertType
     * @param date
     * @param upvotes
     * @param downvotes
     */
    public NeighborhoodAlert(int neighborhoodID, int alertID, int user_id, String alertType,
                             String description, Date date, int upvotes, int downvotes) {
        super(alertID, user_id, alertType, description, date, upvotes, downvotes);
        this.neighborhoodID = neighborhoodID;
        routesAffected = new ArrayList<>();
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
        return routesAffected.add(newRoute);
    }

    /**
     * Returns the list of routes affected
     * @return List of routes affected
     */
    public List<Route> getRoutesAffected() {
        // if no affected routes, return empty list
        if (routesAffected == null) {
            return new ArrayList<>();
        }

        return routesAffected;
    }

    @Override
    public void upvote(int userID, Callback<VoteConfirmation> callback) {
        WMBController controller = WMBController.getInstance();
        final NeighborhoodAlert self = this;
        final Callback<VoteConfirmation> cb = callback;
        controller.neighborhoodAlertUpvote(this.getId(), userID, new Callback<VoteConfirmation>() {
            @Override
            public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
                self.upvotes++;
                cb.onResponse(response, retrofit);
            }

            @Override
            public void onFailure(Throwable t) {
                cb.onFailure(t);
            }
        });
    }

    @Override
    public void downvote(int userID, Callback<VoteConfirmation> callback) {
        WMBController controller = WMBController.getInstance();
        final NeighborhoodAlert self = this;
        final Callback<VoteConfirmation> cb = callback;
        controller.neighborhoodAlertDownvote(this.getId(), userID, new Callback<VoteConfirmation>() {
            @Override
            public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
                self.downvotes++;
                cb.onResponse(response, retrofit);
            }

            @Override
            public void onFailure(Throwable t) {
                cb.onFailure(t);
            }
        });
    }

    /**
     * gets the comments associated with this alert and handles them with the callback.
     * @param callback
     */
    @Override
    public void getComments(Callback<List<Comment>> callback) {
        WMBController controller = WMBController.getInstance();
        controller.getNeighborhoodAlertComments(this.getId(), callback);
    }

    @Override
    protected void checkRep() {
        super.checkRep();
        // Assert.assertTrue(neighborhood != null);
        // Assert.assertTrue(routesAffected != null);
        // Assert.assertFalse(routesAffected.contains(null));
    }
}
