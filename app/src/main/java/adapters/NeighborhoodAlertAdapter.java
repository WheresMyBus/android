package adapters;

import android.content.Context;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import modules.NeighborhoodAlert;
import modules.Route;
import modules.UserDataManager;

/**
 * Created by lesli_000 on 11/12/2016.
 */

public class NeighborhoodAlertAdapter extends ArrayAdapter<NeighborhoodAlert> {
    private static final int MAX_TYPE_LENGTH = 20;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    /**
     * Constructs a NeighborhoodAlertAdapter (used for the alert forum)
     *
     * @param context Context
     * @param resource the resource ID for a layout file
     * @param alerts the list of alerts for the neighborhood
     */
    public NeighborhoodAlertAdapter(Context context, int resource, List<NeighborhoodAlert> alerts) {
        super(context, resource, alerts);
    }

    /**
     * Gets a view that displays the data at the specified position in the data set
     *
     * @param position the position of the alert in the list of alerts
     * @param convertView the old view to reuse, if possible
     * @param parent the parent that this view will eventually be attached to
     * @return the view corresponding to the alert at the specified position
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the property we are displaying
        NeighborhoodAlert alert = getItem(position);

        // inflates a new row
        convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.simple_neighborhood_alert_row, parent, false);

        // get references to specific views so we can populate them with data
        TextView alertType = (TextView) convertView.findViewById(R.id.alert_type);
        TextView routesAffected = (TextView) convertView.findViewById(R.id.routes_affected);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        ImageButton thumbsUp = (ImageButton) convertView.findViewById(R.id.thumbs_up);
        TextView numThumbsUp = (TextView) convertView.findViewById(R.id.num_thumbs_up);
        ImageButton thumbsDown = (ImageButton) convertView.findViewById(R.id.thumbs_down);
        TextView numThumbsDown = (TextView) convertView.findViewById(R.id.num_thumbs_down);

        TimeZone zone = TimeZone.getDefault();
        if (dateFormatter == null) {
            dateFormatter = new SimpleDateFormat("E, MMM d");
            dateFormatter.setTimeZone(zone);
        }
        if (timeFormatter == null) {
            timeFormatter = new SimpleDateFormat("h:mm a");
            timeFormatter.setTimeZone(zone);
        }

        Date alertDate = alert.getDate();

        // gets the header displayed to the user for routes affected
        String routes = routesAffected.getText().toString();

        // gets the set of routes affected from the alert
        List<Route> routesSet = alert.getRoutesAffected();
        if (routesSet.isEmpty()) {
            // reports no routes affected
            routes += " none";
        } else if (routesSet.size() > 5) {
            routes += " several";
        } else {
            // adds the numbers of the routes affected to the string that will be displayed
            boolean firstRoute = true;
            for (Route route : routesSet) {
                if (firstRoute) {
                    // special formatting case for the first route number added
                    routes += " " + route.getNumber();
                    firstRoute = false;
                } else {
                    routes += ", " + route.getNumber();
                }
            }
        }

        // determine if the user has already downVoted/upVoted the alert
        UserDataManager userDataManager = UserDataManager.getManager();
        boolean alertIsUpVoted = userDataManager.getUpVotedAlertsByID().contains(alert.getId());
        boolean alertIsDownVoted = userDataManager.getDownVotedAlertsByID().contains(alert.getId());

        // fill each view with associated data and set image button on click listeners
        String type = alert.getType();
        if (type.length() > MAX_TYPE_LENGTH) {
            alertType.setText(type.substring(0, MAX_TYPE_LENGTH) + "...");
        } else {
            alertType.setText(type);
        }
        routesAffected.setText(routes);
        date.setText(dateFormatter.format(alertDate));
        time.setText(timeFormatter.format(alertDate));
        thumbsUp.setOnClickListener(new ThumbsUpListener(alert, alertIsUpVoted, numThumbsUp));
        numThumbsUp.setText(alert.getUpvotes() + "");
        thumbsDown.setOnClickListener(new ThumbsDownListener(alert, alertIsDownVoted, numThumbsDown));
        numThumbsDown.setText(alert.getDownvotes() + "");

        // color the thumbsUp/thumbsDown buttons if this user has already clicked those buttons
        // in a previous session
        if (alertIsUpVoted) {
            int green = ContextCompat.getColor(getContext(), R.color.green);
            thumbsUp.setColorFilter(green);
        }
        if (alertIsDownVoted) {
            int orange = ContextCompat.getColor(getContext(), R.color.orange);
            thumbsDown.setColorFilter(orange);
        }
        return convertView;
    }
}
