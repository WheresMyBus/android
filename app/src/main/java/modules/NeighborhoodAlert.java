package modules;

import android.util.Pair;

import java.util.Date;
import java.util.Set;

/**
 * Created by lidav on 10/25/2016.
 *
 * Stores data about a alert of a specific neighborhood
 * Invariant: id = -1 if id has not been set, else id > 0
 *          neighborhood, date, type, coordinates != null
 */

public class NeighborhoodAlert extends Alert {
    private Neighborhood neighborhood;
    private Set<Route> routesAffected;

    /**
     * Constructs a NeighborhoodAlert with id initialized to -1
     * and upvotes/downvotes initialized to 0
     * @param neighborhood neighborhood of the alert
     * @param date Date the alert was posted
     * @param type type of alert posted
     * @param coordinates position of alert
     * @param description the text describing the details of the alert
     * @param creatorID id of the creator of the post
     * @throws IllegalArgumentException if any of neighborhood, date, type,
     *              description, coordinates are null
     * @throws IllegalArgumentException if creatorID < 1
     */
    public NeighborhoodAlert(Neighborhood neighborhood, Date date, String description,
                             String type, Pair<Double, Double> coordinates, int creatorID,
                             Set<Route> routesAffected) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets deep copy of Neighborhood this alert is related to
     * @return neighborhood of this alert
     */
    public Route getNeighborhood() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Adds new routes to the set of routes possibly affected by this neighborhood alert.
     * @return true if none of the elements of routesAffected were already stored in the set of
     *              routes affected by this alert, or false otherwise
     */
    public boolean addAffectedRoutes(Set<Route> routesAffected) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
}
