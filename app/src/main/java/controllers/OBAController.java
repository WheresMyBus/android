package controllers;

import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import modules.*;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.Callback;


/**
 * Created by lidav on 10/23/2016.
 *
 * Controller that receives information from OneBusAway
 */

public class OBAController {
    private static OBAController instance = null;

    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create();

    private static RetrofitAPI retrofitService = new Retrofit.Builder()
            .baseUrl("http://wheresmybus-api.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitAPI.class);

    /**
     * Constructs an OBAController and connects to OBA API
     */
    protected OBAController() {

    }

    public static synchronized OBAController getInstance() {
        if(instance == null) {
            instance = new OBAController();
        }
        return instance;
    }

    /**
     * Sets the target for API requests to the production backend.
     * (this is also the default URL)
     */
    public static void useProdURL() {
        retrofitService = new Retrofit.Builder()
                .baseUrl("http://wheresmybus-api.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetrofitAPI.class);
    }

    /**
     * Sets the target for API requests to be the test backend.
     */
    public static void useTestURL() {
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
    public static void useMockURL(String url) {
        retrofitService = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetrofitAPI.class);
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
            for (Route r : routeList) {
                Log.d("route name: ", r.getName());
            }
            Log.d("routeList.isEmpty():", "" + routeList.isEmpty());
            return routeList;
        } catch (Exception e) {
            Log.d("In getRoutesSynch: ", e.toString());
            return new HashSet<>();
        }
    }

    /**
     * Gets a complete set of BusStops for a given route
     * Returns an empty set if the Route was not found or request failed
     * @param route Route to find BusStops on
     * @throws IllegalArgumentException if Route is null
     * @return Set of BusStops for a Route.
     * Returns an empty set if the Route was not found or request failed
     */
    public Set<BusStop> getBusStops(Route route) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets a complete List of Busses for a given route
     * Returns an empty List if Route was not found or request failed.
     * @param route Route to find Busses on
     * @throws IllegalArgumentException if Route is null
     * @return List of Busses for a route, empty if Route not found or request failed
     */
    public List<Bus> getBusses(Route route) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets the location of a specific bus
     * @param bus Bus to get the location of
     * @throws IllegalArgumentException if Bus is null
     * @return Pair<Double,Double> location of where the bus is, or null if Bus not found
     * or if the request failed.
     */
    public Pair<Double,Double> getBusLocation(Bus bus) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
}
