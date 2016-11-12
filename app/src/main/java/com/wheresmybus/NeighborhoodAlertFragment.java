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
import android.widget.GridView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import controllers.WMBController;
import modules.Neighborhood;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NeighborhoodAlertFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NeighborhoodAlertFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner neighborhoodSpinner;
    //private GridView alertTypes;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;

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
                loadSpinnerData(getListStrings(data));
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

    private void loadSpinnerData(List<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this.getActivity(), android.R.layout.simple_spinner_item, data);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        neighborhoodSpinner.setAdapter(adapter);
    }

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
        neighborhoodSpinner = (Spinner) getActivity().findViewById(R.id.neighborhood_spinner);
        neighborhoodSpinner.setOnItemSelectedListener(this);
        //alertTypes = (GridView) getActivity().findViewById(R.id.alert_types);
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
        String neighborhoodName = parent.getItemAtPosition(position).toString();
        // do something to create alert
        // make next items visible
    }

    // TODO: for neighborhood spinner
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
