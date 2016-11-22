package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wheresmybus.NeighborhoodAlertFragment;
import com.wheresmybus.R;

import java.util.ArrayList;
import java.util.List;

import modules.Route;

/**
 * Created by lesli_000 on 11/20/2016.
 */

public class SpinnerAdapter extends ArrayAdapter<Route> {
    private List<Route> alreadyChecked;

    /**
     * Constructs a SpinnerAdapter (used for neighborhood alerts)
     *
     * @param context Context
     * @param resource the resource ID for a layout file
     * @param routes the list of routes
     */
    public SpinnerAdapter(Context context, int resource, List<Route> routes) {
        super(context, resource, routes);
        alreadyChecked = new ArrayList<>();
    }

    /**
     * Gets a view that displays the data at the specified position in the data set
     *
     * @param position the position of the data in the dataset
     * @param convertView the old view to reuse, if possible
     * @param parent the parent that this view will eventually be attached to
     * @return the view corresponding to the data at the specified position
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // get the property we are displaying
        Route route = getItem(position);

        // inflates a new row
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_spinner_row,
                    parent, false);
        }

        // get references to specific views so we can populate them with data
        // CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        TextView number = (TextView) convertView.findViewById(R.id.number);
        TextView name = (TextView) convertView.findViewById(R.id.name);

        // fill each view with associated data
        checkBox.setOnClickListener(new CheckBoxListener(route));
        if (alreadyChecked.contains(route)) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        number.setText(route.getNumber());
        name.setText(route.getName());

        return convertView;
    }

    /**
     * Get the list of routes affected
     *
     * @return the list of routes affected
     */
    public List<Route> getRoutesAffected() {
        return alreadyChecked;
    }

    /**
     * A listener for the checkboxes that controls the onClicks
     */
    private class CheckBoxListener implements View.OnClickListener {
        private Route route;

        public CheckBoxListener(Route route) {
            this.route = route;
        }

        /**
         * Adds a route to the list of routes affected when a checkbox is checked,
         * removes a route from the list when a checkbox is unchecked
         *
         * @param v the current view
         */
        @Override
        public void onClick(View v) {
            boolean checked = ((CheckBox) v).isChecked();
            if (checked) {
                alreadyChecked.add(route);
            } else {
                if (alreadyChecked.contains(route)) {
                    alreadyChecked.remove(route);
                }
            }
        }
    }
}
