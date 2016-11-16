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
 *         route, date, type, coordinates != null
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
     * @param coordinates position of alert
     * @param creatorID id of the creator of the post
     * @param description the text describing the details of the alert
     * @throws IllegalArgumentException if any of route, date, type,
     *                      coordinates, description are null
     * @throws IllegalArgumentException if creatorID < 1
     */
    public RouteAlert(Route route, Date date, String type, Pair<Double, Double> coordinates,
            String description, String creatorID) {
        super(description,date,type,creatorID,coordinates);
        if(route == null) {
            throw new IllegalArgumentException("route == null");
        }
        this.route = route;
        checkRep();
    }

    public RouteAlert(String routeID, int alertID, String user_id, String alertType,
                      String description, Date date, int upvotes, int downvotes) {
        super(alertID, user_id, alertType, description, date, upvotes, downvotes);
        this.routeID = routeID;
    }

    /**
     * Gets deep copy of Route this alert is related to
     * @return route of this alert
     */
    public Route getRoute() {
        return new Route(route.getNumber(), route.getName(), route.getId());
    }

    @Override
    public void upvote(String userID, Callback<VoteConfirmation> callback) {
        WMBController controller = WMBController.getInstance();
        final RouteAlert self = this;
        final Callback<VoteConfirmation> cb = callback;
        controller.routeAlertUpvote(this.getId(), userID, new Callback<VoteConfirmation>() {
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
    public void downvote(String userID, Callback<VoteConfirmation> callback) {
        WMBController controller = WMBController.getInstance();
        final RouteAlert self = this;
        final Callback<VoteConfirmation> cb = callback;
        controller.routeAlertDownvote(this.getId(), userID, new Callback<VoteConfirmation>() {
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
     * gets the comments on this alert.
     * @param callback
     */
    @Override
    public void getComments(Callback<List<Comment>> callback) {
        WMBController controller = WMBController.getInstance();
        controller.getRouteAlertComments(this.getId(), callback);
    }

    @Override
    protected void checkRep() {
        super.checkRep();
        // Assert.assertFalse(route == null);
    }
}
