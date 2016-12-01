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
import java.util.List;
import java.util.Set;

import controllers.WMBController;
import modules.Neighborhood;
import adapters.NeighborhoodAdapter;
import modules.UserDataManager;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NeighborhoodCatalogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NeighborhoodCatalogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NeighborhoodCatalogFragment extends Fragment implements AdapterView.OnItemClickListener {
    // the fragment initialization parameters
    private boolean favoritesOnly;

    private ListView neighborhoodList;
    private List<Neighborhood> neighborhoods;
    private NeighborhoodAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public NeighborhoodCatalogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NeighborhoodCatalogFragment.
     */
    public static NeighborhoodCatalogFragment newInstance(boolean switchOn) {
        NeighborhoodCatalogFragment fragment = new NeighborhoodCatalogFragment();
        Bundle args = new Bundle();
        args.putBoolean("SWITCH_ON", switchOn);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Retrieves the list of neighborhoods from the database to populate the catalog
     *
     * @param savedInstanceState previously saved state, or null
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        neighborhoods = new ArrayList<Neighborhood>();
        favoritesOnly = getArguments() != null && getArguments().getBoolean("SWITCH_ON");
        try {
            neighborhoodRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the neighborhoods from the database
     *
     * @throws Exception if the request fails
     */
    private void neighborhoodRequest() throws Exception {
        WMBController controller = WMBController.getInstance();
        controller.getNeighborhoods(new Callback<List<Neighborhood>>() {
            @Override
            public void onResponse(Response<List<Neighborhood>> response, Retrofit retrofit) {
                List<Neighborhood> data = response.body();
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
     * @param data the list of neighborhoods to be loaded
     */
    private void loadListData(List<Neighborhood> data) {
        adapter = new NeighborhoodAdapter(this.getActivity(),
                android.R.layout.simple_list_item_1, data, true);
        neighborhoodList.setAdapter(adapter);
        for (int i = 0; i < adapter.getCount(); i++) {
            neighborhoods.add(adapter.getItem(i));
        }
        // if accessed from clicking view favorites, show only favorites
        if (favoritesOnly) {
            adapter.clear();
            adapter.addAll(filterFavorites(neighborhoods));
        }
    }

    /**
     * Filters the list of neighborhoods to a new list of only favorites
     *
     * @param neighborhoods The full, unfiltered list of neighborhoods
     * @return a filtered list of only favorite neighborhoods
     */
    private List<Neighborhood> filterFavorites(List<Neighborhood> neighborhoods) {
        List<Neighborhood> favoriteList = new ArrayList<Neighborhood>();
        Set<Integer> favoriteNeighborhoodsByID = UserDataManager.getManager().getFavoriteNeighborhoodsByID();
        for (Neighborhood neighborhood : neighborhoods) {
            if (favoriteNeighborhoodsByID.contains(neighborhood.getID())) {
                favoriteList.add(neighborhood);
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
        View view = inflater.inflate(R.layout.fragment_neighborhood_catalog, container, false);

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
                    adapter.addAll(filterFavorites(neighborhoods));
                } else {
                    adapter.addAll(neighborhoods);
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
        neighborhoodList = (ListView) getActivity().findViewById(R.id.neighborhood_list);
        neighborhoodList.setOnItemClickListener(this);
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
     * When a neighborhood is clicked, it takes the user to the corresponding
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
        intent.putExtra("IS_ROUTE", false);
        Neighborhood neighborhood = (Neighborhood) adapterView.getItemAtPosition(position);
        intent.putExtra("NEIGHBORHOOD", neighborhood);
        intent.putExtra("NEIGHBORHOOD_ID", neighborhood.getID());
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
