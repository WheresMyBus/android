package adapters;

import android.content.Context;
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

import modules.NeighborhoodAlert;
import modules.Route;

/**
 * Created by lesli_000 on 11/12/2016.
 */

public class NeighborhoodAlertAdapter extends ArrayAdapter<NeighborhoodAlert> {
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    //constructor, call on creation
    public NeighborhoodAlertAdapter(Context context, int resource, List<NeighborhoodAlert> alerts) {
        super(context, resource, alerts);
    }

    //called when rendering the list
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the property we are displaying
        NeighborhoodAlert alert = getItem(position);

        // checks if an existing view is being reused, otherwise inflate a new row
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_route_row, parent, false);
        }

        // get references to specific views so we can populate them with data
        TextView alertType = (TextView) convertView.findViewById(R.id.alert_type);
        TextView routesAffected = (TextView) convertView.findViewById(R.id.routes_affected);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        ImageButton thumbsUp = (ImageButton) convertView.findViewById(R.id.thumbs_up);
        TextView numThumbsUp = (TextView) convertView.findViewById(R.id.num_thumbs_up);
        ImageButton thumbsDown = (ImageButton) convertView.findViewById(R.id.thumbs_down);
        TextView numThumbsDown = (TextView) convertView.findViewById(R.id.num_thumbs_down);

        // get strings for the date and time the alert was submitted
        if (dateFormatter == null) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("E, MMM d");
        }
        if (timeFormatter == null) {
            SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
        }

        Date alertDate = alert.getDate();

        // gets the header dispayed to the user for routes affected
        String routes = routesAffected.getText().toString();

        // gets the set of routes affected from the alert
        Set<Route> routesSet = alert.getRoutesAffected();
        if (routesSet.isEmpty()) {
            // reports no routes affected
            routes += " none";
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

        // fill each view with associated data and set image button on click listeners
        alertType.setText(alert.getType());
        routesAffected.setText(routes);
        date.setText(dateFormatter.format(date));
        time.setText(timeFormatter.format(alertDate));
        thumbsUp.setOnClickListener(new ThumbsUpListener());
        numThumbsUp.setText(alert.getUpvotes());
        thumbsDown.setOnClickListener(new ThumbsDownListener());
        numThumbsDown.setText(alert.getDownvotes());

        return convertView;
    }
}
