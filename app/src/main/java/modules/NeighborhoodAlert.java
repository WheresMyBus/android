package modules;

import android.util.Pair;

import java.util.Date;

/**
 * Created by lidav on 10/25/2016.
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
     */
    public NeighborhoodAlert(Neighborhood neighborhood, Date date,
                             String type, Pair<Double, Double> coordinates) {
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
