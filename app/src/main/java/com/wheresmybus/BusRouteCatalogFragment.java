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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import controllers.OBAController;
import controllers.WMBController;
import modules.Neighborhood;
import modules.Route;
import modules.RouteAdapter;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BusRouteCatalogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BusRouteCatalogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusRouteCatalogFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ListView routeList;

    private OnFragmentInteractionListener mListener;

    public BusRouteCatalogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BusRouteCatalogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BusRouteCatalogFragment newInstance(String param1, String param2) {
        BusRouteCatalogFragment fragment = new BusRouteCatalogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Retrieves the list of routes from the database to populate the catalog
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            routeRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the routes from the database
     * @throws Exception if the request fails
     */
    private void routeRequest() throws Exception {
        OBAController controller = OBAController.getInstance();
        controller.getRoutes(new Callback<Set<Route>>() {
            @Override
            public void onResponse(Response<Set<Route>> response, Retrofit retrofit) {
                Set<Route> data = response.body();
                loadListData(setToList(data));
            }

            @Override
            public void onFailure(Throwable t) {
                // stuff to do when it doesn't work
            }
        });
    }

    /**
     * Converts the set of routes returned from the database to a list for the adapter
     * @param routes the set of routes (from the database)
     * @return the list of routes
     */
    private List<Route> setToList(Set<Route> routes) {
        List<Route> data = new ArrayList<Route>();
        for (Route route: routes) {
            data.add(route);
        }
        return data;
    }

    /**
     * Gets the list of route names from the set of routes
     * @param routes set of routes
     * @return the list of route names as strings
     */
    private List<String> getListStrings(Set<Route> routes) {
        List<String> data = new ArrayList<>();
        for (Route route : routes) {
            data.add(route.getName());
        }
        return data;
    }

    /**
     * Load the given data into the ListView
     * @param data the list of strings (route names) to be loaded
     */
    private void loadListData(List<Route> data) {
        RouteAdapter adapter = new RouteAdapter(this.getActivity(),
                android.R.layout.simple_list_item_1, data);
        routeList.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bus_route_catalog, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        routeList = (ListView) getActivity().findViewById(R.id.route_list);
        routeList.setOnItemSelectedListener(this);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    // TODO: for setOnItemSelectedListener
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
