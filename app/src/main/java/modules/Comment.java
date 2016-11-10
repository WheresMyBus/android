package modules;

import junit.framework.Assert;

/**
 * Created by lidav on 10/26/2016.
 * Stores data for a comment that a user posts about an alert
 * Invariant: id = -1 if id has not been set, else id > 0
 *          creatorId > 0
 *          alert, data != null
 */

public class Comment {
    private String data;
    private int creatorId;
    private int upvotes;
    private int downvotes;
    private int id;
    private Alert alert;

    /**
     * Constructs a Comment with upvotes/downvotes/id initialized to 0
     * @param data comment String to store
     * @param creatorId id of the creator of the post
     * @throws IllegalArgumentException if creatorId < 1 or if data is null
     */
    public Comment(String data, int creatorId, Alert alert) {
        if(creatorId < 1 || data == null || alert == null) {
            throw new IllegalArgumentException();
        }
        this.data = data;
        this.creatorId = creatorId;
        this.alert = alert;
        this.id = -1;
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
    public int getCreatorID() {
        return creatorId;
    }

    /**
     * Upvotes this comment
     */
    public void upvote() {
        upvotes++;
        checkRep();
    }

    /**
     * Downvotes this comment
     */
    public void downvote() {
        downvotes++;
        checkRep();
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
     * Returns the alert that this comment is posted to
     * @return alert comment was posted to
     */
    public Alert getAlert() {
        return alert;
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
    private void checkRep() {
        Assert.assertTrue(id == -1 || id > 0);
        Assert.assertTrue(creatorId > 0);
        Assert.assertFalse(data == null);
        Assert.assertTrue(upvotes >= 0);
        Assert.assertTrue(downvotes >= 0);
        Assert.assertFalse(alert == null);
    }
}
