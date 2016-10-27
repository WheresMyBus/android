package modules;

import android.util.Pair;

import java.util.Date;

/**
 * Created by lidav on 10/25/2016.
 *
 * Stores data about a alert of a specific neighborhood
 * Invariant: id = -1 if id has not been set, else id > 0
 *          neighborhood, date, type, coordinates != null
 */

public class NeighborhoodAlert extends Alert {
    private Neighborhood neighborhood;

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
                             String type, Pair<Double, Double> coordinates, int creatorID) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets deep copy of Neighborhood this alert is related to
     * @return neighborhood of this alert
     */
    public Route getNeighborhood() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
}
