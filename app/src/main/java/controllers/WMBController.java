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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import modules.Alert;
import modules.Bus;
import modules.BusStop;
import modules.Comment;
import modules.Neighborhood;
import modules.NeighborhoodAlert;
import modules.RetrofitAPI;
import modules.Route;
import modules.RouteAlert;
import modules.VoteConfirmation;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.Callback;

/**
 * Controller that retrieves information from Where'sMyBus backend server and database
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
    /**
     * Sets the target for API requests to the production backend.
     * (this is also the default URL)
     */
    public void useProdURL() {
        retrofitService = new Retrofit.Builder()
                .baseUrl("http://wheresmybus-api.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetrofitAPI.class);
    }

    /**
     * Sets the target for API requests to be the test backend.
     */
    public void useTestURL() {
        retrofitService = new Retrofit.Builder()
                .baseUrl("https://wheresmybus-api-test.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetrofitAPI.class);
    }

    /**
     * Sets the url for API requests
     * (this should only be used to setup mocks)
     * @param url
     */
    public void useMockURL(String url) {
        retrofitService = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetrofitAPI.class);
    }


    /**
     * Gets a List of all Alerts about a certain route
     * @param routeId id of Route to get alerts of
     * @param callback handles retrofit response containing list of route alerts.
     */
    public void getRouteAlerts(String routeId, Callback<List<RouteAlert>> callback) {
        Call<List<RouteAlert>> call = retrofitService.getRouteAlertsJSON(routeId);
        call.enqueue(callback);
    }

    /**
     * Gets the list of alerts associated with a neighborhood.
     * @param neighborhoodID id of neighborhood whose alerts to get.
     * @param callback handles response containing list of NeighborhoodAlerts.
     */
    public void getNeighborhoodAlerts(int neighborhoodID, Callback<List<NeighborhoodAlert>> callback) {
        Call<List<NeighborhoodAlert>> call = retrofitService.getNeighborhoodAlertsJSON(neighborhoodID);
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
     * @param affectedRouteIds list of affected route ids
     * @param callback handles response containing the generated NeighborhoodAlert.
     */
    public void postAlert(int neighborhoodID, String alertType, String description,
                          String userID, List<String> affectedRouteIds, Callback<NeighborhoodAlert> callback) {
        Call<NeighborhoodAlert> call = retrofitService.postNeighborhoodAlert(neighborhoodID, alertType, description, userID, affectedRouteIds);
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
                          String userID, Callback<RouteAlert> callback) {
        Call<RouteAlert> call = retrofitService.postRouteAlert(routeID,alertType,description,userID);
        call.enqueue(callback);
    }

    /**
     * gets the bus stops in an area.
     * @param latitude of area's center
     * @param longitude of area's center
     * @param radius of area
     * @param callback handles the bus stops in the area.
     */
    public void getBusStops(double latitude,
                            double longitude,
                            int radius,
                            Callback<List<BusStop>> callback) {
        Call<List<BusStop>> call = retrofitService.getBusStops(latitude, longitude, radius);
        call.enqueue(callback);
    }

    // the next two methods are for testing only.
    public NeighborhoodAlert postAlertSynchronously(int neighborhoodID, String alertType, String description,
                                                    String userID, List<String> affectedRouteIds) {
        Call<NeighborhoodAlert> call = retrofitService.postNeighborhoodAlert(neighborhoodID,alertType,description,userID, affectedRouteIds);
        try {
            return call.execute().body();
        } catch (IOException e) {
            Log.d("postAlertSynchronously", e.toString());
            return null;
        }
    }
    public List<NeighborhoodAlert> getAlertsSynchronously(int neighborhoodID) {
        Call<List<NeighborhoodAlert>> call = retrofitService.getNeighborhoodAlertsJSON(neighborhoodID);
        try {
            return call.execute().body();
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
    public void postRouteAlertComment(int routeAlertID, String data, String userID, Callback<Comment> callback) {
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
    public void postNeighborhoodAlertComment(int neighborhoodAlertID, String data, String userID, Callback<Comment> callback) {
        Call<Comment> call = retrofitService.postNeighborhoodAlertComment(neighborhoodAlertID, data, userID);
        call.enqueue(callback);
    }
    // used only for testing.
    public Comment postNeighborhoodAlertCommentSynchronously(int neighborhoodAlertID, String data, String userID) {
        Call<Comment> call = retrofitService.postNeighborhoodAlertComment(neighborhoodAlertID, data, userID);
        try {
            return call.execute().body();
        } catch (IOException e) {
            Log.d("postHoodAlertComment ", e.toString());
            return null;
        }
    }

    /**
     * upvotes a neighborhood alert
     * @param alertID id of the alert
     * @param userID id of this user
     * @param callback handles response containing a VoteConfirmation.
     */
    public void neighborhoodAlertUpvote(int alertID, String userID, Callback<VoteConfirmation> callback) {
        Call<VoteConfirmation> call = retrofitService.postVote("neighborhood_alerts", alertID, "upvote", userID);
        call.enqueue(callback);
    }
    // used for testing only
    public VoteConfirmation neighborhoodAlertUpvoteSynchronously(int alertID, String userID) {
        Call<VoteConfirmation> call = retrofitService.postVote("neighborhood_alerts", alertID, "upvote", userID);
        try {
            return call.execute().body();
        } catch (IOException e) {
            Log.d("neighboralertupvote:", e.toString());
            return null;
        }
    }

    /**
     * downvotes a neighborhood alert
     * @param alertID id of the alert
     * @param userID id of this user
     * @param callback handles response containing a VoteConfirmation
     */
    public void neighborhoodAlertDownvote(int alertID, String userID, Callback<VoteConfirmation> callback) {
        Call<VoteConfirmation> call = retrofitService.postVote("neighborhood_alerts", alertID, "downvote", userID);
        call.enqueue(callback);
    }

    /**
     * upvotes a route alert
     * @param alertID id of the alert
     * @param userID id of this user
     * @param callback handles response containing VoteConfirmation
     */
    public void routeAlertUpvote(int alertID, String userID, Callback<VoteConfirmation> callback) {
        Call<VoteConfirmation> call = retrofitService.postVote("route_alerts", alertID, "upvote", userID);
        call.enqueue(callback);
    }
    /**
     * downvotes a route alert
     * @param alertID id of the alert
     * @param userID id of this user
     * @param callback handles response containing VoteConfirmation
     */
    public void routeAlertDownvote(int alertID, String userID, Callback<VoteConfirmation> callback) {
        Call<VoteConfirmation> call = retrofitService.postVote("route_alerts", alertID, "downvote", userID);
        call.enqueue(callback);
    }

    /**
     * upvotes a comment
     * @param commentID of the comment
     * @param userID of this user
     * @param callback handles the VoteConfimation
     */
    public void commentUpvote(int commentID, String userID, Callback<VoteConfirmation> callback) {
        Call<VoteConfirmation> call = retrofitService.postVote("comments", commentID, "upvote", userID);
        call.enqueue(callback);
    }

    public void commentDownvote(int commentID, String userID, Callback<VoteConfirmation> callback) {
        Call<VoteConfirmation> call = retrofitService.postVote("comments", commentID, "downvote", userID);
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

    public List<Comment> getNeighborhoodAlertCommentsSynchronously(int alertID) {
        Call<List<Comment>> call = retrofitService.getNeighborhoodAlertComments(alertID);
        try {
            return call.execute().body();
        } catch (IOException e) {
            //Log.d("getHoodAlertComment", e.toString());
            return null;
        }
    }

    /**
     * retrieves list of buses for given route from API to be handled by callback.
     * @param routeID id of the route whose buses are needed
     * @param callback handles the list of buses
     */
    public void getBuses(String routeID, Callback<List<Bus>> callback) {
        Call<List<Bus>> call = retrofitService.getBuses(routeID);
        call.enqueue(callback);
    }

    /**
     * delete the previously logged vote.
     * @param alertID
     * @param userID
     * @param callback
     */
    public void neighborhoodAlertUnvote(int alertID, String userID, Callback<VoteConfirmation> callback) {
        Call<VoteConfirmation> call = retrofitService.unvote("neighborhood_alerts", alertID, userID);
        call.enqueue(callback);
    }

    /**
     * delete the previously logged vote.
     * @param alertID
     * @param userID
     * @param callback
     */
    public void routeAlertUnvote(int alertID, String userID, Callback<VoteConfirmation> callback) {
        Call<VoteConfirmation> call = retrofitService.unvote("route_alerts", alertID, userID);
        call.enqueue(callback);
    }

    /**
     * delete the previously logged vote.
     * @param alertID
     * @param userID
     * @param callback
     */
    public void commentUnvote(int alertID, String userID, Callback<VoteConfirmation> callback) {
        Call<VoteConfirmation> call = retrofitService.unvote("comments", alertID, userID);
        call.enqueue(callback);
    }

    /**
     * gets the set of all routes.
     * @param callback handles set of all routes
     */
    public void getRoutes(Callback<Set<Route>> callback) {
        Call<Set<Route>> call = retrofitService.getRoutesJSON();
        call.enqueue(callback);
    }

    /**
     * Gets a complete List of Neighborhoods
     * @return List of Neighborhoods, empty if request failed
     */
    public Set<Route> getRoutesSynchonously() {
        Call<Set<Route>> call = retrofitService.getRoutesJSON();
        try {
            Set<Route> routeList = call.execute().body();
            /* // debug logging.
            for (Route r : routeList) {
                Log.d("route name: ", r.getName());
            }
            Log.d("routeList.isEmpty():", "" + routeList.isEmpty());
            */
            return routeList;
        } catch (Exception e) {
            Log.d("In getRoutesSynch: ", e.toString());
            return new HashSet<>();
        }
    }
}
