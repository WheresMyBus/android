package modules;

import com.google.gson.annotations.SerializedName;

/**
 * This is a class for encapsulating responses by the server to different
 * upvote and downvote requests.
 */
public class VoteConfirmation {
    @SerializedName("upvotes")
    public final int upvotes;
    @SerializedName("downvotes")
    public final int downvotes;

    public VoteConfirmation(int upvoteCount, int downvoteCount) {
        this.upvotes = upvoteCount;
        this.downvotes = downvoteCount;
    }
}
