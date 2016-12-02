package modules;

import android.util.Pair;

import com.google.gson.annotations.SerializedName;

import junit.framework.Assert;

import java.util.Date;
import java.util.List;

import controllers.WMBController;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by lidav on 10/25/2016.
 *
 * Stores data about a alert of a specific route
 * Invariant: id = -1 if id has not been set, else id > 0
 *         date, type != null
 *         either route is not null, or routeID is not null
 */

public class RouteAlert extends Alert {
    private Route route;
    @SerializedName("route_id")
    private String routeID;

    /**
     * Constructs a RouteAlert with id initialized to -1
     * and upvotes/downvotes initialized to 0
     * @param route Route that Alert is on
     * @param date Date the alert was posted
     * @param type type of alert posted
     * @param creatorID id of the creator of the post
     * @param description the text describing the details of the alert
     * @throws IllegalArgumentException if any of route, date, type,
     *                      coordinates, description are null
     * @throws IllegalArgumentException if creatorID < 1
     */
    public RouteAlert(Route route, Date date, String type,
            String description, String creatorID) {
        super(description,date,type,creatorID);
        if(route == null) {
            throw new IllegalArgumentException("route == null");
        }
        this.route = route;
        checkRep();
    }

    /**
     * Contructs a RouteAlert for usage by Retrofit on server response
     * @param routeID id of the route that this alert is on
     * @param alertID id of the alert
     * @param user_id id of the creator of the alert
     * @param alertType type of the alert
     * @param description description of the alert
     * @param date Date the alert was posted
     * @param upvotes number of upvotes the alert has
     * @param downvotes number of downvotes the alert has
     */
    public RouteAlert(String routeID, int alertID, String user_id, String alertType,
                      String description, Date date, int upvotes, int downvotes) {
        super(alertID, user_id, alertType, description, date, upvotes, downvotes);
        this.routeID = routeID;
        checkRep();
    }

    /**
     * Gets deep copy of Route this alert is related to
     * @return route of this alert
     */
    public Route getRoute() {
        return new Route(route.getNumber(), route.getName(), route.getId());
    }

    /**
     * Upvotes the route alert in the server
     * @param userID id of the user who is upvoting the alert
     * @param callback Callback that describes what to do when the server responds
     */
    @Override
    public void upvote(String userID, Callback<VoteConfirmation> callback) {
        WMBController controller = WMBController.getInstance();
        final RouteAlert self = this;
        final Callback<VoteConfirmation> cb = callback;
        controller.routeAlertUpvote(this.getId(), userID, new Callback<VoteConfirmation>() {
            @Override
            public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
                self.setVotes(response.body());
                cb.onResponse(response, retrofit);
            }

            @Override
            public void onFailure(Throwable t) {
                cb.onFailure(t);
            }
        });
    }

    /**
     * Downvotes the route alert in the server
     * @param userID id of the user who is downvoting the alert
     * @param callback Callback that describes what to do when the server responds
     */
    @Override
    public void downvote(String userID, Callback<VoteConfirmation> callback) {
        WMBController controller = WMBController.getInstance();
        final RouteAlert self = this;
        final Callback<VoteConfirmation> cb = callback;
        controller.routeAlertDownvote(this.getId(), userID, new Callback<VoteConfirmation>() {
            @Override
            public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
                self.setVotes(response.body());
                cb.onResponse(response, retrofit);
            }

            @Override
            public void onFailure(Throwable t) {
                cb.onFailure(t);
            }
        });
    }

    /**
     * Removes the vote this user sent previously for this alert
     * @param userID id of the user who is unvoting the alert
     * @param callback Callback that describes what to do when the server responds
     */
    @Override
    public void unvote(String userID, Callback<VoteConfirmation> callback) {
        WMBController controller = WMBController.getInstance();
        final RouteAlert self = this;
        final Callback<VoteConfirmation> cb = callback;
        controller.routeAlertUnvote(this.getId(), userID, new Callback<VoteConfirmation>() {
            @Override
            public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
                self.setVotes(response.body());
                cb.onResponse(response, retrofit);
            }

            @Override
            public void onFailure(Throwable t) {
                cb.onFailure(t);
            }
        });
    }

    /**
     * gets the comments on this alert.
     * @param callback Callback that describes what to do when the server responds.
     */
    @Override
    public void getComments(Callback<List<Comment>> callback) {
        WMBController controller = WMBController.getInstance();
        controller.getRouteAlertComments(this.getId(), callback);
    }

    @Override
    protected void checkRep() {
        super.checkRep();
        Assert.assertFalse(route == null && routeID == null);
    }
}
