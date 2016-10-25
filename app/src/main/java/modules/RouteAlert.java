package modules;

import android.util.Pair;

import java.util.Date;

/**
 * Created by lidav on 10/25/2016.
 */

public class RouteAlert extends Alert {
    private Route route;

    /**
     * Constructs a RouteAlert with id initialized to -1
     * and upvotes/downvotes initialized to 0
     * @param route Route that Alert is on
     * @param date Date the alert was posted
     * @param type type of alert posted
     * @param coordinates position of alert
     */
    public RouteAlert(Route route, Date date, String type, Pair<Double, Double> coordinates) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets deep copy of Route this alert is related to
     * @return route of this alert
     */
    public Route getRoute() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
}
