package controllers;

import android.util.JsonReader;
import android.util.Pair;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import modules.Alert;
import modules.Comment;
import modules.Neighborhood;
import modules.NeighborhoodAlert;
import modules.RetrofitAPI;
import modules.RouteAlert;
import modules.VoteConfirmation;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.Callback;

/**
 * Created by lidav on 10/23/2016.
 *
 * Controller that receives information from Where'sMyBus backend server and database
 */

public class WMBController {
    private static WMBController instance = null;
    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create();

    private static RetrofitAPI retrofitService = new Retrofit.Builder()
            .baseUrl("http://wheresmybus-api.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(RetrofitAPI.class);

    /**
     * Constructs a WMBController and connects to server/database
     */
    private WMBController() {

    }

    public static synchronized WMBController getInstance() {
        if(instance == null) {
            instance = new WMBController();
        }
        return instance;
    }

    public static void useTestURL() {
        retrofitService = new Retrofit.Builder()
                .baseUrl("https://wheresmybus-api-test.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetrofitAPI.class);
    }

    public static void useMockURL(String url) {
        retrofitService = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetrofitAPI.class);
    }


    /**
     * Gets a list of all alerts
     * @return List of all Alerts, empty if request failed
     */
    public List<Alert> getAlerts() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets a List of all Alerts about a certain route
     * @param routeNumber number of Route to get alerts of
     * @param callback handles retrofit response containing list of route alerts.
     */
    public void getRouteAlerts(String routeNumber, Callback<List<RouteAlert>> callback) {
        Call<List<RouteAlert>> call = retrofitService.getRouteAlertsJSON(routeNumber);
        call.enqueue(callback);
    }

    /**
     * Gets the list of alerts associated with a neighborhood.
     * @param neighborhood the neighborhood whose alerts to get.
     * @param callback handles retrofit response containing list of neighborhood alerts.
     */
    public void getAlerts(Neighborhood neighborhood, Callback<List<NeighborhoodAlert>> callback) {
        Call<List<NeighborhoodAlert>> call = retrofitService.getNeighborhoodAlertsJSON(neighborhood.getID());
        call.enqueue(callback);
    }

    /**
     * Gets a List of all Alerts within a radius of a position
     * @param center Center of the circle to search for alerts in
     * @param radius Radius to search for alerts in
     * @throws IllegalArgumentException if radius < 0
     * @return List of alerts, empty if request failed or no alerts were found
     */
    public List<Alert> getAlerts(Pair<Double,Double> center, double radius) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * post alert for a neighborhood.
     * @param neighborhoodID ID of the neighborhood
     * @param alertType the type of the alert
     * @param description description of the alert
     * @param userID id of the user posting the alert
     * @param callback handles response containing the generated NeighborhoodAlert.
     */
    public void postAlert(int neighborhoodID, String alertType, String description,
                          int userID, Callback<NeighborhoodAlert> callback) {
        Call<NeighborhoodAlert> call = retrofitService.postNeighborhoodAlert(neighborhoodID, alertType, description, userID);
        call.enqueue(callback);
    }

    /**
     * post alert for a route.
     * @param routeID ID of the route
     * @param alertType type of the alert (e.g. construction)
     * @param description description of the alert
     * @param userID id of the user posting the alert
     * @param callback handles response containing the generated RouteAlert
     */
    public void postAlert(String routeID, String alertType, String description,
                          int userID, Callback<RouteAlert> callback) {
        Call<RouteAlert> call = retrofitService.postRouteAlert(routeID,alertType,description,userID);
        call.enqueue(callback);
    }

    // the next two methods are for testing.
    public NeighborhoodAlert postAlertSynchronously(int neighborhoodID, String alertType, String description,
                                                    int userID) {
        Call<NeighborhoodAlert> call = retrofitService.postNeighborhoodAlert(neighborhoodID,alertType,description,userID);
        try {
            NeighborhoodAlert alert = call.execute().body();
            return alert;
        } catch (IOException e) {
            Log.d("postAlertSynchronously", e.toString());
            return null;
        }
    }
    public List<NeighborhoodAlert> getAlertsSynchronously(int neighborhoodID) {
        Call<List<NeighborhoodAlert>> call = retrofitService.getNeighborhoodAlertsJSON(neighborhoodID);
        try {
            List<NeighborhoodAlert> alerts = call.execute().body();
            return alerts;
        } catch (IOException e) {
            Log.d("getAlertsSynchronously", e.toString());
            return null;
        }
    }

    /**
     * posts a comment on a given route alert.
     * @param routeAlertID id of route alert comment belongs to
     * @param data the content of the comment
     * @param userID id of this user
     * @param callback handles response containing the generated Comment
     */
    public void postRouteAlertComment(int routeAlertID, String data, int userID, Callback<Comment> callback) {
        Call<Comment> call = retrofitService.postRouteAlertComment(routeAlertID,data,userID);
        call.enqueue(callback);
    }

    /**
     * posts a comment on a given neighborhood alert
     * @param neighborhoodAlertID id of neighborhood alert the comment belongs to
     * @param data the content of the comment
     * @param userID id of this user
     * @param callback handles response containing the generated Comment
     */
    public void postNeighborhoodAlertComment(int neighborhoodAlertID, String data, int userID, Callback<Comment> callback) {
        Call<Comment> call = retrofitService.postNeighborhoodAlertComment(neighborhoodAlertID, data, userID);
        call.enqueue(callback);
    }

    /**
     * upvotes a neighborhood alert
     * @param alertID id of the alert
     * @param userID id of this user
     * @param callback handles response containing a VoteConfirmation.
     */
    public void neighborhoodAlertUpvote(int alertID, int userID, Callback<VoteConfirmation> callback) {
        Call<VoteConfirmation> call = retrofitService.postVote("neighborhood_alerts", alertID, "upvote", userID);
        call.enqueue(callback);
    }

    /**
     * downvotes a neighborhood alert
     * @param alertID id of the alert
     * @param userID id of this user
     * @param callback handles response containing a VoteConfirmation
     */
    public void neighborhoodAlertDownvote(int alertID, int userID, Callback<VoteConfirmation> callback) {
        Call<VoteConfirmation> call = retrofitService.postVote("neighborhood_alerts", alertID, "downvote", userID);
        call.enqueue(callback);
    }

    /**
     * upvotes a route alert
     * @param alertID id of the alert
     * @param userID id of this user
     * @param callback handles response containing VoteConfirmation
     */
    public void routeAlertUpvote(int alertID, int userID, Callback<VoteConfirmation> callback) {
        Call<VoteConfirmation> call = retrofitService.postVote("route_alerts", alertID, "upvote", userID);
        call.enqueue(callback);
    }
    /**
     * downvotes a route alert
     * @param alertID id of the alert
     * @param userID id of this user
     * @param callback handles response containing VoteConfirmation
     */
    public void routeAlertDownvote(int alertID, int userID, Callback<VoteConfirmation> callback) {
        Call<VoteConfirmation> call = retrofitService.postVote("route_alerts", alertID, "downvote", userID);
        call.enqueue(callback);
    }

    /**
     * upvotes a neighborhood alert comment
     * @param commentID id of the comment
     * @param userID id of this user
     * @param callback handles response containing VoteConfirmation
     */
    public void neighborhoodAlertCommentUpvote(int commentID, int userID, Callback<VoteConfirmation> callback) {
        Call<VoteConfirmation> call = retrofitService.postVote("neighborhood_alert_comments", commentID, "upvote", userID);
        call.enqueue(callback);
    }

    /**
     * downvotes a neighborhood alert comment
     * @param commentID id of the comment
     * @param userID id of the user
     * @param callback handles response containing VoteConfirmation
     */
    public void neighborhoodAlertCommentDownvote(int commentID, int userID, Callback<VoteConfirmation> callback) {
        Call<VoteConfirmation> call = retrofitService.postVote("neighborhood_alert_comments", commentID, "downvote", userID);
        call.enqueue(callback);
    }

    /**
     * upvote a route alert comment
     * @param commentID id of the comment
     * @param userID id of the user
     * @param callback handles response containing VoteConfirmation
     */
    public void routeAlertCommentUpvote(int commentID, int userID, Callback<VoteConfirmation> callback) {
        Call<VoteConfirmation> call = retrofitService.postVote("route_alert_comments", commentID, "upvote", userID);
        call.enqueue(callback);
    }
    /**
     * downvote a route alert comment
     * @param commentID id of the comment
     * @param userID id of the user
     * @param callback handles response containing a VoteConfirmation
     */
    public void routeAlertCommentDownvote(int commentID, int userID, Callback<VoteConfirmation> callback) {
        Call<VoteConfirmation> call = retrofitService.postVote("route_alert_comments", commentID, "downvote", userID);
        call.enqueue(callback);
    }


    /**
     * Performs actions of callback on List of Neighborhoods obtained from api.
     * @param callback a retrofit Callback for handling the response from the api.
     */
    public void getNeighborhoods(Callback<List<Neighborhood>> callback) {
        Call<List<Neighborhood>> call = retrofitService.getNeighborhoodsJSON();
        call.enqueue(callback);
    }

    /**
     * Gets a complete List of Neighborhoods
     * @return List of Neighborhoods, empty if request failed
     */
    public List<Neighborhood> getNeighborhoodsSynchonously() {
        Call<List<Neighborhood>> call = retrofitService.getNeighborhoodsJSON();
        try {
            return call.execute().body();
        } catch (Exception e) {
            return new ArrayList<Neighborhood>();
        }
    }


    /**
     * gets list of all comments on a route alert
     * @param alertID id of the route alert
     * @param callback handles response containing a list of comments.
     */
    public void getRouteAlertComments(int alertID, Callback<List<Comment>> callback) {
        Call<List<Comment>> call = retrofitService.getRouteAlertComments(alertID);
        call.enqueue(callback);
    }


    /**
     * gets list of all comments on a neighborhood alert
     * @param alertID id of the alert
     * @param callback handles response containing a list of comments
     */
    public void getNeighborhoodAlertComments(int alertID, Callback<List<Comment>> callback) {
        Call<List<Comment>> call = retrofitService.getNeighborhoodAlertComments(alertID);
        call.enqueue(callback);
    }

    /**
     * Upvotes a comment in the database
     * @param comment Comment to upvote
     * @return true if upvote succeeded, else false
     */
    public boolean upvote(Comment comment) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Downvotes a comment in the database
     * @param comment Comment to downvote
     * @return true if downvote succeeded, else false
     */
    public boolean downvote(Comment comment) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
}
