package modules;

import android.util.Pair;

import java.util.Date;

/**
 * Created by lidav on 10/23/2016.
 *
 * Describes an Alert class which stores the data for a bus alert and allows up/down voting
 */

public class Alert {
    private int id;
    private Route route;
    private Date date;
    private String type;
    private Pair<Double, Double> coordinates;
    private int upvotes;
    private int downvotes;

    /* constructors */
    public Alert(int id, Route route, Date date, String type, Pair<Double, Double> coordinates,
                 int upvotes, int downvotes) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public boolean upvote() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public  boolean downvote() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public int getId() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public Route getRoute() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public String getType() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public Pair<Double, Double> getCoordinates() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public int getUpvotes() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public int getDownvotes() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
}
