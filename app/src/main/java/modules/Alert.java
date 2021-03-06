package modules;

import android.util.Pair;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import junit.framework.*;

import controllers.WMBController;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by lidav on 10/23/2016.
 *
 * Describes an Alert abstract class which stores the data for a bus alert and allows up/down voting
 * Invariant: id = -1 if id has not been set, else id > 0
 *          date, type, description != null
 *          creatorID != null
 */

public abstract class Alert implements Serializable, Comparable<Alert> {
    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private String creatorID;
    @SerializedName("created_at")
    private Date date;
    @SerializedName("issues")
    private String type;
    @SerializedName("description")
    private String description;
    @SerializedName("upvotes")
    protected int upvotes;
    @SerializedName("downvotes")
    protected int downvotes;

    /**
     * Constructs an alert with id initialized to -1
     * and upvotes/downvotes initialized to 0
     * @param description the text describing the details of the alert
     * @param date Date the alert was posted
     * @param type type of alert as a String
     * @param creatorID id of the creator of the post
     * @throws IllegalArgumentException if any of description, date, type, coordinates are null
     * @throws IllegalArgumentException if creatorID < 1
     */
    public Alert(String description, Date date, String type,
                 String creatorID) {
        if(description == null || creatorID == null || date == null || type == null) {
            if(description == null) {
                throw new IllegalArgumentException("null description");
            } else if (creatorID == null) {
                throw new IllegalArgumentException("null creatorID");
            } else if (date == null) {
                throw new IllegalArgumentException("null date");
            } else if (type == null) {
                throw new IllegalArgumentException("null type");
            }
        }
        this.description = description;
        this.date = (Date) date.clone();
        this.type = type;
        this.creatorID = creatorID;
        this.id = -1;
    }

    /**
     * Constructor used when getting alerts from the server
     * @param alertID id of the alert
     * @param user_id id of the creator of the alert
     * @param alertType type of the alert
     * @param description description of the alert
     * @param date Date the alert was posted
     * @param upvotes number of upvotes the alert has
     * @param downvotes number of downvotes the alert has
     * @throws IllegalArgumentException if description, user_id, date, or alertType are null
     * @throws IllegalArgumentException if alertID is < 1, or if upvotes or downvotes < 0
     */
    public Alert(int alertID, String user_id, String alertType, String description,
                 Date date, int upvotes, int downvotes) {
        if (description == null || user_id == null || date == null || alertType == null) {
            if(description == null) {
                throw new IllegalArgumentException("null description");
            } else if (user_id == null) {
                throw new IllegalArgumentException("null user id");
            } else if (date == null) {
                throw new IllegalArgumentException("null date");
            } else {
                throw new IllegalArgumentException("null alertType");
            }
        }
        if(alertID < 1 || upvotes < 0 || downvotes < 0) {
            if(alertID  < 1) {
                throw new IllegalArgumentException("alertID < 1");
            } else if (upvotes < 0) {
                throw new IllegalArgumentException("upvotes < 0");
            } else {
                throw new IllegalArgumentException("downvotes < 0");
            }
        }
        this.id = alertID;
        this.creatorID = user_id;
        this.date = (Date)date.clone();
        this.type = alertType;
        this.description = description;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    /**
     * Gets the id of the creator of the alert
     * @return creator's id as int
     */
    public String getCreatorID() {
        return creatorID;
    }

    /**
     * Upvotes this alert in the server
     * @param userID id of the user who is upvoting the alert
     * @param callback Callback that describes what to do when the server responds
     */
    public abstract void upvote(String userID, Callback<VoteConfirmation> callback);

    /**
     * Downvotes this alert in the server
     * @param userID id of the user who is downvoting the alert
     * @param callback Callback that describes what to do when the server responds
     */
    public abstract void downvote(String userID, Callback<VoteConfirmation> callback);

    /**
     * removes previously logged vote on this alert in the server
     * @param userID id of the user who is unvoting the alert
     * @param callback Callback that describes what to do when the server responds
     */
    public abstract void unvote(String userID, Callback<VoteConfirmation> callback);

    /**
     * method for getting comments for an alert from the server
     * @param callback Callback that describes what to do when the server responds
     */
    public abstract void getComments(Callback<List<Comment>> callback);

    /**
     * sets the upvotes and downvotes based on the received confirmation.
     * @param confirmation callback that describes what to do when the server responds
     */
    protected void setVotes(VoteConfirmation confirmation) {
        this.upvotes = confirmation.upvotes;
        this.downvotes = confirmation.downvotes;
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

    @Override
    public int compareTo(Alert o) {
        return o.date.compareTo(date);
    }

    protected void checkRep() {
        Assert.assertTrue(date != null);
        Assert.assertTrue(type != null);
        Assert.assertTrue(description != null);
        Assert.assertTrue(upvotes > -1);
        Assert.assertTrue(downvotes > -1);
        Assert.assertTrue(id == -1 || id > 0);
    }
}
