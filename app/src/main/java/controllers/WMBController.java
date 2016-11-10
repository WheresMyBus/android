package controllers;

import android.util.JsonReader;
import android.util.Pair;
import android.util.Log;
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
import modules.RetrofitAPI;
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
    private static RetrofitAPI retrofitService = new Retrofit.Builder()
            .baseUrl("http://wheresmybus-api.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitAPI.class);

    /**
     * Constructs a WMBController and connects to server/database
     */
    protected WMBController() {

    }

    public static synchronized WMBController getInstance() {
        if(instance == null) {
            instance = new WMBController();
        }
        return instance;
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
     * @param routeNumber number of Route to get alerts about
     * @return List of Alerts, null if request failed
     */
    public List<Alert> getAlerts(int routeNumber) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets a List of all Alerts about a certain neighborhood
     * @param neighborhood Neighboorhood to get alerts about
     * @throws IllegalArgumentException if Neighborhood is null
     * @return List of Alerts, empty if request failed
     */
    public List<Alert> getAlerts(Neighborhood neighborhood) {
        throw new UnsupportedOperationException("Not Yet Implemented");
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
     * Posts an alert to the WMB database
     * @param alert alert to post
     * @return true if post succeeded, else false
     */
    public boolean postAlert(Alert alert) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Upvotes an alert in the database
     * @param alert Alert to upvote
     * @return true if upvote succeeded, else false
     */
    public boolean upvote(Alert alert) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
    /**
     * Downvotes an alert in the database
     * @param alert Alert to downvote
     * @return true if downvote succeeded, else false
     */
    public boolean downvote(Alert alert) {
        throw new UnsupportedOperationException("Not Yet Implemented");
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
     * Gets an ordered-by-date list of all comments in the Alert
     * @param alert Alert to get comments about
     * @return list of comments sorted chronologically
     */
    public List<Comment> getComments(Alert alert) {
        throw new UnsupportedOperationException("Not Yet Implemented");
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
