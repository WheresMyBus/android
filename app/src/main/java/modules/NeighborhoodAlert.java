package modules;

import android.util.Pair;

import junit.framework.Assert;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lidav on 10/25/2016.
 *
 * Stores data about a alert of a specific neighborhood
 * Invariant: id = -1 if id has not been set, else id > 0
 *          neighborhood, date, type, coordinates != null
 *          routesAffected != null and all elements of routesAffected != null
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
        super(description, date, type, creatorID, coordinates);
        if(neighborhood == null || routesAffected == null) {
            throw new IllegalArgumentException("null parameters");
        }
        this.neighborhood = neighborhood;
        this.routesAffected = routesAffected;
    }

    /**
     * Gets deep copy of Neighborhood this alert is related to
     * @return neighborhood of this alert
     */
    public Neighborhood getNeighborhood() {
        return new Neighborhood(neighborhood.getID(), neighborhood.getName());
    }

    /**
     * Adds new routes to the set of routes possibly affected by this neighborhood alert.
     * @param newRoute Route to add to affected routes
     * @return true if none of the elements of routesAffected were already stored in the set of
     *              routes affected by this alert, or false otherwise
     * @throws IllegalArgumentException if newRoute is null
     */
    public boolean addAffectedRoute(Route newRoute) {
        if(newRoute == null) {
            throw new IllegalArgumentException();
        }
        return routesAffected.add(newRoute);
    }

    /**
     * Returns the set of routes affected
     * @return Set of routes affected
     */
    public Set<Route> getRoutesAffected() {
        return new HashSet<>(routesAffected);
    }

    @Override
    protected void checkRep() {
        super.checkRep();
        Assert.assertTrue(neighborhood != null);
        Assert.assertTrue(routesAffected != null);
        Assert.assertFalse(routesAffected.contains(null));
    }
}
