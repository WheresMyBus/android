package modules;

import android.util.Pair;

import com.google.gson.annotations.SerializedName;

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
    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private int creatorID;
    @SerializedName("created_at")
    private Date date;
    @SerializedName("issue_type")
    private String type;
    @SerializedName("description")
    private String description;
    private Pair<Double, Double> coordinates;
    @SerializedName("upvotes")
    private int upvotes;
    @SerializedName("downvotes")
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
        this.id = -1;
    }

    /**
     * TODO: document this. Added to construct from server response for beta.
     * @param alertID
     * @param user_id
     * @param alertType
     * @param description
     * @param date
     * @param upvotes
     * @param downvotes
     */
    public Alert(int alertID, int user_id, String alertType, String description,
                 Date date, int upvotes, int downvotes) {
        if (description == null || date == null || alertType == null) {
            throw new IllegalArgumentException("null parameters");
        }
        if (user_id < 1) {
            throw new IllegalArgumentException("User ID < 1");
        }
        this.id = alertID;
        this.creatorID = user_id;
        this.date = date;
        this.type = alertType;
        this.description = description;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

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
        checkRep();
    }

    /**
     * Downvotes this alert
     */
    public void downvote() {
        this.downvotes++;
        checkRep();
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
            checkRep();
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

    protected void checkRep() {
        // Assert.assertTrue(this.creatorID > 0);
        Assert.assertTrue(date != null);
        Assert.assertTrue(type != null);
        Assert.assertTrue(description != null);
        // Assert.assertTrue(coordinates != null);
        Assert.assertTrue(upvotes > -1);
        Assert.assertTrue(downvotes > -1);
        Assert.assertTrue(id == -1 || id > 0);
    }
}
