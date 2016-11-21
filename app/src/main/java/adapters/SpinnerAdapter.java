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

    //constructor, call on creation
    public SpinnerAdapter(Context context, int resource, List<Route> routes) {
        super(context, resource, routes);
        alreadyChecked = new ArrayList<>();
    }

    //called when rendering the list
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

    public List<Route> getRoutesAffected() {
        return alreadyChecked;
    }

    private class CheckBoxListener implements View.OnClickListener {
        private Route route;

        public CheckBoxListener(Route route) {
            this.route = route;
        }

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
