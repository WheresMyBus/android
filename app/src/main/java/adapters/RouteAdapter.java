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

/**
 * Created by lesli_000 on 11/11/2016.
 */

public class RouteAdapter extends ArrayAdapter<Route> {
    private boolean isStarred;

    //constructor, call on creation
    public RouteAdapter(Context context, int resource, List<Route> routes, boolean isStarred) {
        super(context, resource, routes);
        this.isStarred = isStarred;
    }

    //called when rendering the list
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
            CatalogActivity catalogActivity = (CatalogActivity) getContext();

            ImageButton favoriteButton = (ImageButton) convertView.findViewById(R.id.star);
            favoriteButton.setOnClickListener(new FavoriteRouteListener(route.getId(),
                    catalogActivity));
            // TODO: setColorFilter if route is user's favorite
            // TODO: verify if this works
            boolean favorited = catalogActivity.favoriteRoutesByID.contains(route.getId());
            if (favorited) {
                // TODO: set color
                //favoriteButton.setColorFilter(ContextCompat.getColor(convertView.getContext(),
                //        R.color.yellow));
                favoriteButton.callOnClick();
            }
        }

        return convertView;
    }
}
