package modules;

/**
 * Created by lidav on 10/26/2016.
 * Stores data for a comment that a user posts about an alert
 */

public class Comment {
    private String data;
    private int creatorId;
    private int upvotes;
    private int downvotes;
    private int id;

    /**
     * Constructs a Comment with upvotes/downvotes/id initialized to 0
     * @param data comment String to store
     * @param id id of the creator of the post
     */
    public Comment(String data, int id) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets the comment String
     * @return comment String
     */
    public String getData() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets the id of the creator of the alert
     * @return creator's id as int
     */
    public int getCreatorID() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Upvotes this comment
     */
    public void upvote() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Downvotes this comment
     */
    public void downvote() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Sets the id of this comment if id = -1
     * @param id id to set on this alert
     * @return true if id was set correctly, else returns false
     */
    public boolean setId(int id) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets the id of this comment
     * @return id of comment as int
     */
    public int getId() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
    /**
     * Gets the upvotes of this comment
     * @return upvotes as int
     */
    public int getUpvotes() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets the downvotes of this comment
     * @return downvotes as int
     */
    public int getDownvotes() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
}
