package modules;

import android.util.Pair;

import java.util.Date;
import junit.framework.*;

/**
 * Created by lidav on 10/23/2016.
 *
 * Describes an Alert abstract class which stores the data for a bus alert and allows up/down voting
 * Invariant: id = -1 if id has not been set, else id > 0
 *          date, type, coordinates, description != null
 *          creatorID > 0
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
     * @param creatorID id of the creator of the post
     * @throws IllegalArgumentException if any of description, date, type, coordinates are null
     * @throws IllegalArgumentException if creatorID < 1
     */
    public Alert(String description, Date date, String type,
                 int creatorID, Pair<Double, Double> coordinates) {
        if(creatorID < 1) {
            throw new IllegalArgumentException("creatorID < 1");
        }
        if(description == null || date == null || type == null || coordinates == null) {
            throw new IllegalArgumentException("null parameters");
        }
        this.description = description;
        this.date = (Date) date.clone();
        this.type = type;
        this.coordinates = new Pair<>(coordinates.first, coordinates.second);
        this.creatorID = creatorID;
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
        return creatorID;
    }

    /**
     * Upvotes this alert
     */
    public void upvote() {
        this.upvotes++;
    }

    /**
     * Downvotes this alert
     */
    public void downvote() {
        this.downvotes++;
    }

    /**
     * Sets the id of this alert if id = -1
     * @param id id to set on this alert
     * @return true if id was set correctly, else returns false
     * @throws IllegalArgumentException if id < 1
     */
    public boolean setId(int id) {
        if(id < 1) {
            throw new IllegalArgumentException();
        }
        if(this.id == -1) {
            this.id = id;
            return true;
        }
        return false;
    }

    /**
     * Gets the id of this alert
     * @return id of alert as int
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the type of this alert
     * @return type of alert as String
     */
    public String getType() {
        return type;
    }

    /**
     * Gets a deep copy of the coordinates of this alert
     * @return coordinates of this alert as a double Pair
     */
    public Pair<Double, Double> getCoordinates() {
        return new Pair<>(coordinates.first, coordinates.second);
    }

    /**
     * Gets the upvotes of this alert
     * @return upvotes as int
     */
    public int getUpvotes() {
        return upvotes;
    }

    /**
     * Gets the downvotes of this alert
     * @return downvotes as int
     */
    public int getDownvotes() {
        return downvotes;
    }

    /**
     * Gets the description of the alert
     * @return description as String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the Date of the alert
     * @return Date of the alert
     */
    public Date getDate() {
        return (Date) this.date.clone();
    }

    private void checkRep() {
        Assert.assertTrue(this.creatorID > 0);
        Assert.assertTrue(date != null);
        Assert.assertTrue(type != null);
        Assert.assertTrue(description != null);
        Assert.assertTrue(coordinates != null);
        Assert.assertTrue(upvotes > -1);
        Assert.assertTrue(downvotes > -1);
        Assert.assertTrue(id == -1 || id > 0);
    }
}
