package com.wheresmybus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import controllers.OBAController;
import modules.Route;
import adapters.RouteAdapter;
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
public class BusRouteCatalogFragment extends Fragment implements AdapterView.OnItemClickListener {
    // the fragment initialization parameters
    private List<String> favoriteRoutesByID;
    private boolean favoritesOnly;

    private ListView routeList;
    private RouteAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public BusRouteCatalogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param favoriteRoutesByID List of the ids of favorite routes
     * @return A new instance of fragment BusRouteCatalogFragment
     */
    public static BusRouteCatalogFragment newInstance(ArrayList<String> favoriteRoutesByID,
                                                      boolean favoritesOnly) {
        BusRouteCatalogFragment fragment = new BusRouteCatalogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("FAVORITES", favoriteRoutesByID);
        args.putBoolean("FAVORITES_ONLY", favoritesOnly);
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
        favoriteRoutesByID = getArguments() != null ? getArguments().getStringArrayList("FAVORITES") : null;
        favoritesOnly = getArguments() != null && getArguments().getBoolean("FAVORITES_ONLY");
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
                List<Route> data = new ArrayList<Route>(response.body());
                Collections.sort(data);
                loadListData(data);
            }

            @Override
            public void onFailure(Throwable t) {
                // stuff to do when it doesn't work
            }
        });
    }

    /**
     * Filters the list of routes to a new list of only favorites
     *
     * @param routes The full, unfiltered list of routes
     * @return a filtered list of only favorite routes
     */
    private List<Route> filterFavorites(List<Route> routes) {
        List<Route> favoriteList = new ArrayList<Route>();
        for (Route route : routes) {
            if (favoriteRoutesByID.contains(route.getId())) {
                favoriteList.add(route);
            }
        }
        return favoriteList;
    }


    /**
     * Load the given data into the ListView
     * @param data the list of routes to be loaded
     */
    private void loadListData(List<Route> data) {
        if (favoritesOnly) {
            List<Route> favorites = filterFavorites(data);
            adapter = new RouteAdapter(this.getActivity(),
                    android.R.layout.simple_list_item_1, favorites, true);
        } else {
            adapter = new RouteAdapter(this.getActivity(),
                    android.R.layout.simple_list_item_1, data, true);
        }
        routeList.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bus_route_catalog, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        routeList = (ListView) getActivity().findViewById(R.id.route_list);
        routeList.setOnItemClickListener(this);
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

    /**
     * When a route is clicked, it takes the user to the corresponding
     * alert forum page
     * @param adapterView the AdapterView that keeps track of the ListView elements
     * @param view the ListView for this class
     * @param position the position of the element clicked
     * @param id the id of the element clicked
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), AlertForumActivity.class);
        intent.putExtra("ALERT_TYPE", "Route");
        Route route = (Route) adapterView.getItemAtPosition(position);
        intent.putExtra("ROUTE", route);
        intent.putExtra("ROUTE_ID", route.getId());
        startActivity(intent);
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
