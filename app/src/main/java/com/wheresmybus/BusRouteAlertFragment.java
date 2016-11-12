package com.wheresmybus;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import controllers.OBAController;
import modules.Route;
import modules.RouteAdapter;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BusRouteAlertFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BusRouteAlertFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner busRouteSpinner;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private EditText text;

    // information for the alert
    private Route route;
    private List<String> alertTypes;

    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            textView = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    private void busRouteRequest() throws Exception {
        OBAController controller = OBAController.getInstance();
        controller.getRoutes(new Callback<Set<Route>>() {
            @Override
            public void onResponse(Response<Set<Route>> response, Retrofit retrofit) {
                List<Route> data = new ArrayList<Route>(response.body());
                Collections.sort(data);
                loadSpinnerData(data);
            }

            @Override
            public void onFailure(Throwable t) {
                // stuff to do when it doesn't work
            }
        });
    }

    private void loadSpinnerData(List<Route> data) {
        RouteAdapter adapter = new RouteAdapter(this.getActivity(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        busRouteSpinner.setAdapter(adapter);
    }

    /*
    private void loadSpinnerData(List<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this.getActivity(), android.R.layout.simple_spinner_item, data);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        busRouteSpinner.setAdapter(adapter);
    }
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bus_route_alert, container, false);

        busRouteSpinner = (Spinner) view.findViewById(R.id.bus_route_spinner);
        busRouteSpinner.setOnItemSelectedListener(this);

        checkBox1 = (CheckBox) view.findViewById(R.id.checkBox1);
        checkBox1.setOnClickListener(this);
        checkBox2 = (CheckBox) view.findViewById(R.id.checkBox2);
        checkBox2.setOnClickListener(this);
        checkBox3 = (CheckBox) view.findViewById(R.id.checkBox3);
        checkBox3.setOnClickListener(this);
        checkBox4 = (CheckBox) view.findViewById(R.id.checkBox4);
        checkBox4.setOnClickListener(this);

        text = (EditText) view.findViewById(R.id.alert_description);

        alertTypes = new ArrayList<>();

        try {
            busRouteRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // TODO: for spinner's setOnItemSelectedListener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Object route = parent.getItemAtPosition(position);
        if (route instanceof Route) {
            this.route = (Route) route;
        }
    }

    // TODO: for spinner's setOnItemSelectedListener
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void onCheckBoxSelected(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        String alertType;

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBox1:
                alertType = checkBox1.getText().toString().toLowerCase();
                handleAlertType(alertType, checked);
                break;
            case R.id.checkBox2:
                alertType = checkBox2.getText().toString().toLowerCase();
                handleAlertType(alertType, checked);
                break;
            case R.id.checkBox3:
                alertType = checkBox3.getText().toString().toLowerCase();
                handleAlertType(alertType, checked);
                break;
            case R.id.checkBox4:
                alertType = checkBox4.getText().toString().toLowerCase();
                handleAlertType(alertType, checked);
                break;
        }
    }

    private void handleAlertType(String alertType, boolean checked) {
        if (checked) {
            alertTypes.add(alertType);
        } else {
            if (alertTypes.contains(alertType)) {
                alertTypes.remove(alertType);
            }
        }
    }

    public Route getRoute() {
        return route;
    }

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

    public String getDescription() {
        return text.getText().toString();
    }

    @Override
    public void onClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        String alertType;

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBox1:
                alertType = checkBox1.getText().toString();
                handleAlertType(alertType, checked);
                break;
            case R.id.checkBox2:
                alertType = checkBox2.getText().toString();
                handleAlertType(alertType, checked);
                break;
            case R.id.checkBox3:
                alertType = checkBox3.getText().toString();
                handleAlertType(alertType, checked);
                break;
            case R.id.checkBox4:
                alertType = checkBox4.getText().toString();
                handleAlertType(alertType, checked);
                break;
        }
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
