package com.wheresmybus;

import android.util.Pair;

import java.util.Date;

import modules.RouteAlert;
import modules.NeighborhoodAlert;

/**
 * Created by lidav on 10/23/2016.
 */

public class AlertCreate {

    /**
     * Creates an alert for a given route
     * @param routeNumber the number of the route to create an alert for
     * @param description the text describing the details of the alert
     * @param date Date the alert was posted
     * @param coordinates position the alert was posted from
     * @param creatorID id of the creator of the post
     * @return a new alert for a specific route
     */
    public RouteAlert createNewRouteAlert(int routeNumber, String description, Date date,
                                          int creatorID, Pair<Double, Double> coordinates) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Creates an alert for a given neighborhood
     * @param neighborhood the name of the neighborhood to create an alert for
     * @param description the text describing the details of the alert
     * @param date Date the alert was posted
     * @param coordinates position the alert was posted from
     * @param creatorID id of the creator of the post
     * @return a new alert for a specific neighborhood
     */
    public NeighborhoodAlert createNewNeighborhoodAlert(String neighborhood, String description,
                                                        Date date, int creatorID, Pair<Double, Double> coordinates) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
}
