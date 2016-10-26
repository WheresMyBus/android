package controllers;

import android.util.Pair;

import java.util.List;
import java.util.Set;
import modules.*;

/**
 * Created by lidav on 10/23/2016.
 *
 * Controller that receives information from OneBusAway
 */

public class OBAController {
    /**
     * Constructs an OBAController and connects to OBA API
     */
    public OBAController() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets a complete set of Routes
     * Returns an empty set if the request failed
     * @return Set of routes from One Bus Away, empty if request failed
     */
    public Set<Route> getRoutes() {
        throw new UnsupportedOperationException("Not Yet Implemented");
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
