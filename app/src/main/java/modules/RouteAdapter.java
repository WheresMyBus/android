package modules;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wheresmybus.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import modules.Route;

/**
 * Created by lesli_000 on 11/11/2016.
 */

public class RouteAdapter extends ArrayAdapter<Route> {

    private Context context;
    private List<Route> routes;

    //constructor, call on creation
    public RouteAdapter(Context context, int resource, List<Route> routes) {
        super(context, resource, routes);

        this.context = context;
        this.routes = routes;
    }

    //called when rendering the list
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the property we are displaying
        Route route = getItem(position);

        // checks if an existing view is being reused, otherwise inflate a new row
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_row, parent, false);
        }

        // get references to specific views so we can populate them with data
        TextView number = (TextView) convertView.findViewById(R.id.number);
        TextView name = (TextView) convertView.findViewById(R.id.name);

        // fill each view with associated data
        number.setText(route.getNumber());
        name.setText(route.getName());

        return convertView;
    }
}
