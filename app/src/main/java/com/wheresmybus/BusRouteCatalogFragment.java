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
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import controllers.WMBController;
import modules.Route;
import adapters.RouteAdapter;
import modules.UserDataManager;
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
    private boolean favoritesOnly;

    private ListView routeList;
    private List<Route> routes;
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
    public static BusRouteCatalogFragment newInstance(boolean switchOn) {
        BusRouteCatalogFragment fragment = new BusRouteCatalogFragment();
        Bundle args = new Bundle();
        args.putBoolean("SWITCH_ON", switchOn);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Retrieves the list of routes from the database to populate the catalog
     *
     * @param savedInstanceState previously saved state, or null
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        routes = new ArrayList<Route>();
        favoritesOnly = getArguments() != null && getArguments().getBoolean("SWITCH_ON");
        try {
            routeRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the routes from the database
     *
     * @throws Exception if the request fails
     */
    private void routeRequest() throws Exception {
        WMBController controller = WMBController.getInstance();
        controller.getRoutes(new Callback<List<Route>>() {
            @Override
            public void onResponse(Response<List<Route>> response, Retrofit retrofit) {
                List<Route> data = response.body();
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
     * Load the given data into the ListView
     *
     * @param data the list of routes to be loaded
     */
    private void loadListData(List<Route> data) {
        adapter = new RouteAdapter(this.getActivity(),
                android.R.layout.simple_list_item_1, data, true);
        routeList.setAdapter(adapter);
        for (int i = 0; i < adapter.getCount(); i++) {
            routes.add(adapter.getItem(i));
        }
        // if accessed from clicking view favorites, show only favorites
        if (favoritesOnly) {
            adapter.clear();
            adapter.addAll(filterFavorites(routes));
        }
    }

    /**
     * Filters the list of routes to a new list of only favorites
     *
     * @param routes The full, unfiltered list of routes
     * @return a filtered list of only favorite routes
     */
    private List<Route> filterFavorites(List<Route> routes) {
        List<Route> favoriteList = new ArrayList<Route>();
        Set<String> favoriteRoutesByID = UserDataManager.getManager().getFavoriteRoutesByID();
        for (Route route : routes) {
            if (favoriteRoutesByID.contains(route.getId())) {
                favoriteList.add(route);
            }
        }
        return favoriteList;
    }

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
        View view = inflater.inflate(R.layout.fragment_bus_route_catalog, container, false);

        // set up the view favorites only switch
        final Switch favoriteSwitch = (Switch) view.findViewById(R.id.favoriteSwitch);
        if (favoritesOnly) {
            favoriteSwitch.setChecked(true);
        }
        favoriteSwitch.setOnClickListener(new View.OnClickListener() {
            /**
             * When checked, shows only favorites; otherwise all routes
             *
             * @param view the current view
             */
            @Override
            public void onClick(View view) {
                adapter.clear();
                if (favoriteSwitch.isChecked()) {
                    adapter.addAll(filterFavorites(routes));
                } else {
                    adapter.addAll(routes);
                }
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    /**
     * Part of the call structure that sets up the fragment to be displayed.
     *
     * @param savedInstanceState previously saved state, or null
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        routeList = (ListView) getActivity().findViewById(R.id.route_list);
        routeList.setOnItemClickListener(this);
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
     * When a route is clicked, it takes the user to the corresponding
     * alert forum page
     *
     * @param adapterView the AdapterView that keeps track of the ListView elements
     * @param view the ListView for this class
     * @param position the position of the element clicked
     * @param id the id of the element clicked
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), AlertForumActivity.class);
        intent.putExtra("IS_ROUTE", true);
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
        void onFragmentInteraction(Uri uri);
    }
}
