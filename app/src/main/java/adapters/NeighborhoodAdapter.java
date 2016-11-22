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
import com.wheresmybus.FavoriteNeighborhoodListener;
import com.wheresmybus.FavoriteRouteListener;
import com.wheresmybus.R;

import java.util.List;

import modules.Neighborhood;
import modules.UserDataManager;

/**
 * Created by lesli_000 on 11/11/2016.
 */

public class NeighborhoodAdapter extends ArrayAdapter<Neighborhood> {
    private boolean isStarred;

    /**
     * Constructs a NeighborhoodAdapter (used for the neighborhood catalog)
     *
     * @param context Context
     * @param resource the resource ID for a layout file
     * @param neighborhoods the list of neighborhoods
     * @param isStarred a boolean for whether or not the row is starred
     */
    public NeighborhoodAdapter(Context context, int resource, List<Neighborhood> neighborhoods,
                               boolean isStarred) {
        super(context, resource, neighborhoods);
        this.isStarred = isStarred;
    }

    /**
     * Get a view that displays the data at the specified position in the data set
     *
     * @param position the position of the neighborhood in the list of neighborhoods
     * @param convertView the old view to reuse, if possible
     * @param parent the parent that this view will eventually be attached to
     * @return the view corresponding to the data at the specified position
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the property we are displaying
        Neighborhood neighborhood = getItem(position);

        // checks if an existing view is being reused, otherwise inflate a new row
        if (isStarred) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.simple_starred_neighborhood_row, parent, false);
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.simple_neighborhood_row, parent, false);
        }

        // get references to specific views so we can populate them with data
        TextView name = (TextView) convertView.findViewById(R.id.name);

        // fill each view with associated data
        name.setText(neighborhood.getName());

        if (isStarred) {

            ImageButton favoriteButton = (ImageButton) convertView.findViewById(R.id.star);
            favoriteButton.setOnClickListener(
                    new FavoriteNeighborhoodListener(neighborhood.getID())
            );

            // true if the user had this neighborhood favorited when the main activity
            // last stopped (MainActivity.onStop() was called)
            boolean favorited = UserDataManager.getManager()
                    .getFavoriteNeighborhoodsByID()
                    .contains(neighborhood.getID());
            if (favorited) {
                // this colors the button and toggles the listener on, so that
                // the next click will un-color the button
                favoriteButton.callOnClick();
            }

        }

        return convertView;
    }
}