package modules;

import android.util.Pair;

import junit.framework.Assert;

import java.util.Date;

/**
 * Created by lidav on 10/25/2016.
 *
 * Stores data about a alert of a specific route
 * Invariant: id = -1 if id has not been set, else id > 0
 *         route, date, type, coordinates != null
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
     * @param creatorID id of the creator of the post
     * @param description the text describing the details of the alert
     * @throws IllegalArgumentException if any of route, date, type,
     *                      coordinates, description are null
     * @throws IllegalArgumentException if creatorID < 1
     */
    public RouteAlert(Route route, Date date, String type, Pair<Double, Double> coordinates,
            String description, int creatorID) {
        super(description,date,type,creatorID,coordinates);
        if(route == null) {
            throw new IllegalArgumentException("route == null");
        }
        this.route = route;
        checkRep();
    }

    /**
     * Gets deep copy of Route this alert is related to
     * @return route of this alert
     */
    public Route getRoute() {
        return new Route(route.getNumber(), route.getName(), route.getId());
    }

    @Override
    protected void checkRep() {
        super.checkRep();
        Assert.assertFalse(route == null);
    }
}
