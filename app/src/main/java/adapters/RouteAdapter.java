package adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wheresmybus.CatalogActivity;
import com.wheresmybus.FavoriteRouteListener;
import com.wheresmybus.R;

import java.util.List;

import modules.Route;
import modules.UserDataManager;

/**
 * Created by lesli_000 on 11/11/2016.
 */

public class RouteAdapter extends ArrayAdapter<Route> {
    private boolean isStarred;

    /**
     * Constructs a RouteAdapter (used for the route catalog)
     *
     * @param context Context
     * @param resource the resource ID for a layout file
     * @param routes the list of routes
     * @param isStarred a boolean for whether or not the row is starred
     */
    public RouteAdapter(Context context, int resource, List<Route> routes, boolean isStarred) {
        super(context, resource, routes);
        this.isStarred = isStarred;
    }

    /**
     * Get a view that displays the data at the specified position in the data set
     *
     * @param position the position of the route in the list of routes
     * @param convertView the old view to reuse, if possible
     * @param parent the parent that this view will eventually be attached to
     * @return the view corresponding to the route at the specified position
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the property we are displaying
        Route route = getItem(position);

        // checks if an existing view is being reused, otherwise inflate a new row
        if (isStarred) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.simple_starred_route_row, parent, false);
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.simple_route_row, parent, false);
        }

        // get references to specific views so we can populate them with data
        TextView number = (TextView) convertView.findViewById(R.id.number);
        TextView name = (TextView) convertView.findViewById(R.id.name);

        // fill each view with associated data
        number.setText(route.getNumber());
        name.setText(route.getName());

        if (isStarred) {

            ImageButton favoriteButton = (ImageButton) convertView.findViewById(R.id.star);
            favoriteButton.setOnClickListener(
                    new FavoriteRouteListener(route.getId())
            );

            // see NeighborhoodAdapter for an explanation of what this does
            boolean favorited = UserDataManager.getManager()
                    .getFavoriteRoutesByID()
                    .contains(route.getId());
            if (favorited) {
                favoriteButton.callOnClick();
            }
        }
        return convertView;
    }
}
