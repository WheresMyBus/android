package modules;

import com.google.gson.annotations.SerializedName;

/**
 * This is a class for encapsulating responses by the server to different
 * upvote and downvote requests.
 */
public class VoteConfirmation {
    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private String userID;
    @SerializedName("value")
    private String value;

    public VoteConfirmation(int id, String userID, String value) {
        this.id = id;
        this.userID = userID;
        this.value = value;
    }

    public int getId() {
        return id;
    }
    public String getUserID() {
        return userID;
    }
    public String getValue() {
        return value;
    }
}
