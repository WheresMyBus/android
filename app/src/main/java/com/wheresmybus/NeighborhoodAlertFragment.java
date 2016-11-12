package com.wheresmybus;

import android.app.Activity;
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
import android.widget.GridView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import controllers.WMBController;
import modules.Alert;
import modules.Neighborhood;
import modules.NeighborhoodAdapter;
import modules.NeighborhoodAlert;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NeighborhoodAlertFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NeighborhoodAlertFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Spinner neighborhoodSpinner;
    //private GridView alertTypes;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private EditText text;

    // information for the alerts
    private Neighborhood neighborhood;
    private List<String> alertTypes;

    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
        try {
            neighborhoodRequest();
            //loadCheckBoxData(getResources().getStringArray(R.array.neighborhood_alert_types));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void neighborhoodRequest() throws Exception {
        WMBController controller = WMBController.getInstance();
        controller.getNeighborhoods(new Callback<List<Neighborhood>>() {
            @Override
            public void onResponse(Response<List<Neighborhood>> response, Retrofit retrofit) {
                List<Neighborhood> data = response.body();
                loadSpinnerData(data);
            }

            @Override
            public void onFailure(Throwable t) {
                // stuff to do when it doesn't work
            }
        });
    }

    private List<String> getListStrings(List<Neighborhood> neighborhoods) {
        List<String> data = new ArrayList<>();
        for (Neighborhood neighborhood : neighborhoods) {
            data.add(neighborhood.getName());
        }
        return data;
    }

    private void loadSpinnerData(List<Neighborhood> data) {
        NeighborhoodAdapter adapter = new NeighborhoodAdapter(this.getActivity(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        neighborhoodSpinner.setAdapter(adapter);
    }

    /*
    private void loadSpinnerData(List<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this.getActivity(), android.R.layout.simple_spinner_item, data);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        neighborhoodSpinner.setAdapter(adapter);
    }
    */

    private void loadCheckBoxData(String[] data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_list_item_multiple_choice, data);
        //alertTypes.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_neighborhood_alert, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

        text = (EditText) activity.findViewById(R.id.alert_description);

        alertTypes = new ArrayList<>();
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

    // TODO: for neighborhood spinner
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

    public void onCheckBoxSelected(View view) {
        // Is the view now checked?
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

    private void handleAlertType(String alertType, boolean checked) {
        if (checked) {
            alertTypes.add(alertType);
        } else {
            if (alertTypes.contains(alertType)) {
                alertTypes.remove(alertType);
            }
        }
    }

    public Neighborhood getNeighborhood() {
        return neighborhood;
    }

    // assumes alertTypes.size() > 0
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
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        String alertType;

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBox5:
                alertType = checkBox1.getText().toString();
                handleAlertType(alertType, checked);
                break;
            case R.id.checkBox6:
                alertType = checkBox2.getText().toString();
                handleAlertType(alertType, checked);
                break;
            case R.id.checkBox7:
                alertType = checkBox3.getText().toString();
                handleAlertType(alertType, checked);
                break;
            case R.id.checkBox8:
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
