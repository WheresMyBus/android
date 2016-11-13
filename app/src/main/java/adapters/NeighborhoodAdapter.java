package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wheresmybus.FavoriteListener;
import com.wheresmybus.R;

import java.util.List;

import modules.Neighborhood;

/**
 * Created by lesli_000 on 11/11/2016.
 */

public class NeighborhoodAdapter extends ArrayAdapter<Neighborhood> {
    private boolean isStarred;

    // constructor, call on creation
    public NeighborhoodAdapter(Context context, int resource, List<Neighborhood> neighborhoods,
                               boolean isStarred) {
        super(context, resource, neighborhoods);
        this.isStarred = isStarred;
    }

    // called when rendering the list
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // get the property we are displaying
        Neighborhood neighborhood = getItem(position);

        // checks if an existing view is being reused, otherwise inflate a new row
        if (convertView == null) {
            if (isStarred) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.simple_starred_neighborhood_row, parent, false);
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.simple_neighborhood_row, parent, false);
            }
        }

        // get references to specific views so we can populate them with data
        TextView name = (TextView) convertView.findViewById(R.id.name);

        // fill each view with associated data
        name.setText(neighborhood.getName());

        if (isStarred) {
            ImageButton favoriteButton = (ImageButton) convertView.findViewById(R.id.star);
            favoriteButton.setOnClickListener(new FavoriteListener());
        }

        return convertView;
    }
}