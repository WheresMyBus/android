package modules;

import android.util.Pair;

import java.util.Date;

/**
 * Created by lidav on 10/23/2016.
 *
 * Describes an Alert class which stores the data for a bus alert and allows up/down voting
 * Invariant: id = -1 if id has not been set, else id > 0
 *          date, type, coordinates != null
 */

public abstract class Alert {
    private int id;
    private int creatorID;
    private Date date;
    private String type;
    private String description;
    private Pair<Double, Double> coordinates;
    private int upvotes;
    private int downvotes;

    /**
     * Constructs an alert with id initialized to -1
     * and upvotes/downvotes initialized to 0
     * @param description the text describing the details of the alert
     * @param date Date the alert was posted
     * @param type type of alert as a String
     * @param coordinates position the alert was posted from
     */
    public Alert(String description, Date date, String type,
                 int creatorID, Pair<Double, Double> coordinates) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Default constructor
     */
    public Alert() {}

    /**
     * Gets the id of the creator of the alert
     * @return creator's id as int
     */
    public int getCreatorID() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Upvotes this alert
     */
    public void upvote() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Downvotes this alert
     */
    public void downvote() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Sets the id of this alert if id = -1
     * @param id id to set on this alert
     * @return true if id was set correctly, else returns false
     */
    public boolean setId(int id) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets the id of this alert
     * @return id of alert as int
     */
    public int getId() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets the type of this alert
     * @return type of alert as String
     */
    public String getType() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets a deep copy of the coordinates of this alert
     * @return coordinates of this alert as a double Pair
     */
    public Pair<Double, Double> getCoordinates() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets the upvotes of this alert
     * @return upvotes as int
     */
    public int getUpvotes() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets the downvotes of this alert
     * @return downvotes as int
     */
    public int getDownvotes() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
}
