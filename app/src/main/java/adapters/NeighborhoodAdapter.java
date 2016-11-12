package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wheresmybus.R;

import java.util.List;

import modules.Neighborhood;

/**
 * Created by lesli_000 on 11/11/2016.
 */

public class NeighborhoodAdapter extends ArrayAdapter<Neighborhood> {

    // constructor, call on creation
    public NeighborhoodAdapter(Context context, int resource, List<Neighborhood> neighborhoods) {
        super(context, resource, neighborhoods);
    }

    // called when rendering the list
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // get the property we are displaying
        Neighborhood neighborhood = getItem(position);

        // checks if an existing view is being reused, otherwise inflate a new row
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_neighborhood_row, parent, false);
        }

        // get references to specific views so we can populate them with data
        TextView name = (TextView) convertView.findViewById(R.id.name);

        // fill each view with associated data
        name.setText(neighborhood.getName());

        return convertView;
    }
}