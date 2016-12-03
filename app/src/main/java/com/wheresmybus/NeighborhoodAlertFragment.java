package com.wheresmybus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import adapters.RouteAdapter;
import adapters.SpinnerAdapter;
import controllers.OBAController;
import controllers.WMBController;
import modules.Neighborhood;
import adapters.NeighborhoodAdapter;
import modules.Route;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NeighborhoodAlertFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NeighborhoodAlertFragment extends Fragment implements
        AdapterView.OnItemSelectedListener, View.OnClickListener {
    // references to layout structures where users submit alert data
    private Spinner neighborhoodSpinner;
    //private GridView alertTypes;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private EditText text;

    private SpinnerAdapter spinnerAdapter;
    private List<Route> routes;

    // information for the alerts
    private Neighborhood neighborhood;
    private List<String> alertTypes;

    private OnFragmentInteractionListener mListener;

    /**
     * Part of the call structure to display the activity.
     *
     * @param savedInstanceState previously saved state, or null
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
        try {
            neighborhoodRequest();
            busRouteRequest();
            //loadCheckBoxData(getResources().getStringArray(R.array.neighborhood_alert_types));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Requests a list of neighborhoods from the Where's My Bus controller and loads it into the
     * spinner where the user will select a neighborhood for which to submit an alert.
     *
     * @throws Exception if the request fails
     */
    private void neighborhoodRequest() throws Exception {
        WMBController controller = WMBController.getInstance();
        controller.getNeighborhoods(new Callback<List<Neighborhood>>() {
            @Override
            public void onResponse(Response<List<Neighborhood>> response, Retrofit retrofit) {
                List<Neighborhood> data = response.body();
                Collections.sort(data);
                loadSpinnerData(data);
            }

            @Override
            public void onFailure(Throwable t) {
                // stuff to do when it doesn't work
            }
        });
    }

    /**
     * Loads the given list of neighborhoods into the spinner on the layout for this fragment.
     *
     * @param data the list of neighborhoods to be imported into the spinner.
     */
    private void loadSpinnerData(List<Neighborhood> data) {
        NeighborhoodAdapter adapter = new NeighborhoodAdapter(this.getActivity(),
                android.R.layout.simple_spinner_item, data, false);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        neighborhoodSpinner.setAdapter(adapter);
        if (neighborhood != null) {
            int spinnerPosition = adapter.getPosition(neighborhood);
            neighborhoodSpinner.setSelection(spinnerPosition);
            neighborhoodSpinner.setEnabled(false);
        }
    }

    public void setSpinner(Neighborhood neighborhood) {
        this.neighborhood = neighborhood;
    }

    /*
    private void loadCheckBoxData(String[] data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_list_item_multiple_choice, data);
        //alertTypes.setAdapter(adapter);
    }
    */

    /**
     * Part of the call structure that sets up the fragment to be displayed.
     *
     * @param inflater used to inflate any views in the fragment
     * @param container parent view that the fragment's UI should be attached to
     * @param savedInstanceState previously saved state, or null
     * @return the view for the fragment's UI, or null
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_neighborhood_alert, container, false);
    }

    /**
     * Part of the call structure to display this activity.
     *
     * @param savedInstanceState previously saved state, or null
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // grab references to the different structures where users will submit alert data
        Activity activity = getActivity();
        neighborhoodSpinner = (Spinner) activity.findViewById(R.id.neighborhood_spinner);
        neighborhoodSpinner.setOnItemSelectedListener(this);
        //alertTypes = (GridView) getActivity().findViewById(R.id.alert_types);

        checkBox1 = (CheckBox) activity.findViewById(R.id.checkBox5);
        checkBox1.setOnClickListener(this);
        checkBox2 = (CheckBox) activity.findViewById(R.id.checkBox6);
        checkBox2.setOnClickListener(this);
        checkBox3 = (CheckBox) activity.findViewById(R.id.checkBox7);
        checkBox3.setOnClickListener(this);
        checkBox4 = (CheckBox) activity.findViewById(R.id.checkBox8);
        checkBox4.setOnClickListener(this);

        text = (EditText) activity.findViewById(R.id.neighborhood_alert_description);

        getView().findViewById(R.id.other_type_description).setVisibility(View.INVISIBLE);

        alertTypes = new ArrayList<>();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * Part of the call structure that sets up the fragment to be displayed.
     *
     * @param context Context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Part of the fragment call structure.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Stores the neighborhood the user selected in the spinner.
     *
     * @param parent the adapter that keeps track of the list elements
     * @param view the view containing the selectable elements
     * @param position the position of the element clicked
     * @param id the id of the element clicked
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Object neighborhood = parent.getItemAtPosition(position);
        if (neighborhood instanceof Neighborhood) {
            this.neighborhood = (Neighborhood) neighborhood;
        }
    }

    // TODO: for neighborhood spinner
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Returns the neighborhood the user selected from the spinner or null if the user has not
     * selected a neighborhood yet.
     *
     * @return the neighborhood the user selected from the spinner or null if the user has not
     * selected a neighborhood yet.
     */
    public Neighborhood getNeighborhood() {
        return neighborhood;
    }

    /**
     * Returns a string representing the types the user selected for the alert, with different
     * types separated by a comma and a space if there were multiple, or null if the user has not
     * selected any types yet.
     *
     * @return a string representing the alert types the user selected or null if the user has not
     * selected any types yet
     */
    public String getAlertType() {
        if (alertTypes == null || alertTypes.size() == 0) {
            return null;
        } else {
            String alertType = alertTypes.get(0);
            for (int i = 1; i < alertTypes.size(); i++) {
                alertType += ", " + alertTypes.get(i);
            }
            return alertType;
        }
    }

    /**
     * Gets the description of the alert type if the "other" box is checked, null otherwise
     *
     * @return the description of the alert type; null if no text entered yet or "other" box not checked
     */
    public String getOtherType() {
        if (checkBox4.isChecked()) {
            EditText otherText = (EditText) getView().findViewById(R.id.other_type_description);
            return otherText.getText().toString();
        } else {
            return null;
        }
    }

    /**
     * Returns the description the user inputted for the alert or null if the user has not entered
     * a description yet.
     *
     * @return the description the user entered or null if the user has not written one yet.
     */
    public String getDescription() {
        return text.getText().toString();
    }

    public List<Route> getRoutesAffected() {
        if (spinnerAdapter == null) {
            return new ArrayList<>();
        } else {
            return spinnerAdapter.getRoutesAffected();
        }
    }

    /**
     * Implements the View.OnClickListener interface. Determines which checkboxes the user has
     * checked and stores the alert types associated with those boxes.
     *
     * @param view the checkbox clicked
     */
    @Override
    public void onClick(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        String alertType;
        EditText otherType = (EditText) getView().findViewById(R.id.other_type_description);

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBox5:
                alertType = checkBox1.getText().toString();
                handleAlertType(alertType, checked);
                otherType.setVisibility(View.INVISIBLE);
                break;
            case R.id.checkBox6:
                alertType = checkBox2.getText().toString();
                handleAlertType(alertType, checked);
                otherType.setVisibility(View.INVISIBLE);
                break;
            case R.id.checkBox7:
                alertType = checkBox3.getText().toString();
                handleAlertType(alertType, checked);
                otherType.setVisibility(View.INVISIBLE);
                break;
            case R.id.checkBox8:
                alertType = checkBox4.getText().toString();
                handleAlertType(alertType, checked);
                // only show EditText for alert type if "other" is checked
                if (checkBox4.isChecked()) {
                    otherType.setVisibility(View.VISIBLE);
                } else {
                    otherType.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    /**
     * Adds the given alertType to a list of alert types if the checkbox associated with it was
     * checked or otherwise ensures the list does not contain it.
     *
     * @param alertType the alert type to be added to the list if the checkbox was checked or
     *                  otherwise to be kept out of the list
     * @param checked indicates if the checkbox associated with the given alertType was checked
     */
    private void handleAlertType(String alertType, boolean checked) {
        if (checked) {
            alertTypes.add(alertType);
        } else {
            if (alertTypes.contains(alertType)) {
                alertTypes.remove(alertType);
            }
        }
    }

    public void openRouteDialog(View view) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this.getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this.getActivity());
        }
        builder.setTitle("Select affected route(s):");
        try {
            ListView spinner = new ListView(this.getActivity());
            if (spinnerAdapter == null) {
                spinnerAdapter = new SpinnerAdapter(this.getActivity(),
                        android.R.layout.simple_spinner_item, routes);
            }
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            spinner.setAdapter(spinnerAdapter);
            /*spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Route route = (Route) parent.getItemAtPosition(position);
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
                    boolean checked = checkBox.isChecked();
                    if (checked) {
                        routesAffected.add(route);
                    } else {
                        if (routesAffected.contains(route)) {
                            routesAffected.remove(route);
                        }
                    }
                }
            });*/
            builder.setView(spinner);
            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        builder.show();
    }

    /**
     * Requests a set of bus routes from the OneBusAway controller and loads the routes in
     * the spinner on the layout for this fragment to let users select a route for which they will
     * submit an alert.
     *
     * @throws Exception
     */
    private void busRouteRequest() throws Exception {
        OBAController controller = OBAController.getInstance();
        controller.getRoutes(new Callback<Set<Route>>() {
            @Override
            public void onResponse(Response<Set<Route>> response, Retrofit retrofit) {
                List<Route> data = new ArrayList<Route>(response.body());
                Collections.sort(data);
                routes = data;
            }

            @Override
            public void onFailure(Throwable t) {
                // stuff to do when it doesn't work
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
