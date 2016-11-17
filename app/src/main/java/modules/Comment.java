package modules;

import com.google.gson.annotations.SerializedName;

import junit.framework.Assert;

import java.util.Date;

import controllers.WMBController;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by lidav on 10/26/2016.
 * Stores data for a comment that a user posts about an alert
 * Invariant: id = -1 if id has not been set, else id > 0
 *          creatorId > 0
 *          alert, data != null
 */

public class Comment {
    @SerializedName("message")
    private String data;
    @SerializedName("user_id")
    private String creatorID;
    @SerializedName("upvotes")
    private int upvotes;
    @SerializedName("downvotes")
    private int downvotes;
    @SerializedName("id")
    private int id;
    @SerializedName("created_at")
    private Date date;

    /**
     * Constructs a Comment with upvotes/downvotes initialized to 0, id initialized to -1
     * @param data comment String to store
     * @param date creation date of the comment
     * @param creatorId id of the creator of the post
     * @throws IllegalArgumentException if creatorId < 1 or if data is null
     */
    public Comment(String data, String creatorId, Date date) {
        if(creatorId == null || data == null) {
            throw new IllegalArgumentException();
        }
        this.data = data;
        this.creatorID = creatorId;
        this.date = (Date)date.clone();
        this.id = -1;
        checkRep();
    }
    /**
     * Constructs a Comment with upvotes/downvotes/id as parameters
     * @param data comment String to store
     * @param date creation date of the comment
     * @param creatorId id of the creator of the post
     * @param upvotes upvotes to initialize the comment to
     * @param downvotes downvotes to initialize the comment to
     * @throws IllegalArgumentException if creatorId < 1 or if data is null
     * @throws IllegalArgumentException if upvotes, downvotes < 0, or if id is 0 or < -1
     */
    public Comment(int id, String creatorId, String data, int upvotes, int downvotes, Date date) {
        this(data, creatorId, date);
        setId(id);
        if(upvotes < 0 || downvotes < 0) {
            throw new IllegalArgumentException();
        }
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        checkRep();
    }
    /**
     * Gets the comment String
     * @return comment String
     */
    public String getData() {
        return data;
    }

    /**
     * Gets the id of the creator of the alert
     * @return creator's id as int
     */
    public String getCreatorID() {
        return creatorID;
    }

    /**
     * Upvotes this comment
     */
    public void upvote(String userID, Callback<VoteConfirmation> callback) {
        WMBController controller = WMBController.getInstance();
        final Comment self = this;
        final Callback<VoteConfirmation> cb = callback;
        controller.commentUpvote(this.getId(), userID, new Callback<VoteConfirmation>() {
            @Override
            public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
                self.upvotes++;
                cb.onResponse(response,retrofit);
            }

            @Override
            public void onFailure(Throwable t) {
                cb.onFailure(t);
            }
        });
    }

    /**
     * Downvotes this comment
     */
    public void downvote(String userID, Callback<VoteConfirmation> callback) {
        WMBController controller = WMBController.getInstance();
        final Comment self = this;
        final Callback<VoteConfirmation> cb = callback;
        controller.commentDownvote(this.getId(), userID, new Callback<VoteConfirmation>() {
            @Override
            public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
                self.downvotes++;
                cb.onResponse(response,retrofit);
            }

            @Override
            public void onFailure(Throwable t) {
                cb.onFailure(t);
            }
        });
    }

    /**
     * Sets the id of this comment if id = -1
     * @param id id to set on this alert
     * @return true if id was set correctly, else returns false
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
     * Gets the id of this comment
     * @return id of comment as int
     */
    public int getId() {
        return id;
    }
    /**
     * Gets the upvotes of this comment
     * @return upvotes as int
     */
    public int getUpvotes() {
        return upvotes;
    }

    /**
     * Gets the downvotes of this comment
     * @return downvotes as int
     */
    public int getDownvotes() {
        return downvotes;
    }

    public Date getDate() { return (Date) this.date.clone(); }


    private void checkRep() {
        Assert.assertTrue(id == -1 || id > 0);
        Assert.assertFalse(creatorID ==  null);
        Assert.assertFalse(data == null);
        Assert.assertTrue(upvotes >= 0);
        Assert.assertTrue(downvotes >= 0);
    }
}
