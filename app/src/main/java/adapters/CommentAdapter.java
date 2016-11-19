package adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wheresmybus.R;
import com.wheresmybus.ThumbsDownListener;
import com.wheresmybus.ThumbsUpListener;

import java.util.List;

import modules.Comment;
import modules.UserDataManager;

/**
 * Created by lesli_000 on 11/15/2016.
 */

public class CommentAdapter extends ArrayAdapter<Comment> {

    // constructor, call on creation
    public CommentAdapter(Context context, int resource, List<Comment> neighborhoods) {
        super(context, resource, neighborhoods);
    }

    // called when rendering the list
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // get the property we are displaying
        Comment comment = getItem(position);

        // inflate a new row
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_comment, parent,
                false);

        // get references to specific views so we can populate them with data
        TextView message = (TextView) convertView.findViewById(R.id.comment);
        ImageButton thumbsUp = (ImageButton) convertView.findViewById(R.id.thumbs_up);
        TextView numThumbsUp = (TextView) convertView.findViewById(R.id.num_thumbs_up);
        ImageButton thumbsDown = (ImageButton) convertView.findViewById(R.id.thumbs_down);
        TextView numThumbsDown = (TextView) convertView.findViewById(R.id.num_thumbs_down);

        // determine if the user has already downVoted/upVoted the alert
        UserDataManager userDataManager = UserDataManager.getManager();
        boolean commentIsUpVoted = userDataManager
                .getUpVotedAlertsByID()
                .contains(comment.getId());
        boolean commentIsDownVoted = userDataManager
                .getDownVotedAlertsByID()
                .contains(comment.getId());

        // fill each view with associated data
        message.setText(comment.getData());
        thumbsUp.setOnClickListener(new ThumbsUpListener(comment, commentIsUpVoted));
        numThumbsUp.setText(comment.getUpvotes() + "");
        thumbsDown.setOnClickListener(new ThumbsDownListener(comment, commentIsDownVoted));
        numThumbsDown.setText(comment.getDownvotes() + "");

        // color the thumbsUp/thumbsDown buttons if this user has already clicked those buttons
        // in a previous session
        if (commentIsUpVoted) {
            int green = ContextCompat.getColor(getContext(), R.color.green);
            thumbsUp.setColorFilter(green);
        }
        if (commentIsDownVoted) {
            int orange = ContextCompat.getColor(getContext(), R.color.orange);
            thumbsDown.setColorFilter(orange);
        }
        return convertView;
    }
}
