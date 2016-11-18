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
import java.util.List;

import controllers.WMBController;
import modules.Neighborhood;
import adapters.NeighborhoodAdapter;
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
    private List<Integer> favoriteNeighborhoodsByID;
    private boolean favoritesOnly;

    private ListView neighborhoodList;
    private NeighborhoodAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public NeighborhoodCatalogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param favoriteNeighborhoodsByID List of the ids of favorite neighborhoods
     * @return A new instance of fragment NeighborhoodCatalogFragment.
     */
    public static NeighborhoodCatalogFragment newInstance(ArrayList<Integer> favoriteNeighborhoodsByID,
                                                          boolean favoritesOnly) {
        NeighborhoodCatalogFragment fragment = new NeighborhoodCatalogFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList("FAVORITES", favoriteNeighborhoodsByID);
        args.putBoolean("FAVORITES_ONLY", favoritesOnly);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Retrieves the list of neighborhoods from the database to populate the catalog
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoriteNeighborhoodsByID = getArguments() != null ? getArguments().getIntegerArrayList("FAVORITES") : null;
        favoritesOnly = getArguments() != null ? getArguments().getBoolean("FAVORITES_ONLY") : false;
        try {
            neighborhoodRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the neighborhoods from the database
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
     * Filters the list of neighborhoods to a new list of only favorites
     *
     * @param neighborhoods The full, unfiltered list of neighborhoods
     * @return a filtered list of only favorite neighborhoods
     */
    private List<Neighborhood> filterFavorites(List<Neighborhood> neighborhoods) {
        List<Neighborhood> favoriteList = new ArrayList<Neighborhood>();
        for (Neighborhood neighborhood : neighborhoods) {
            if (favoriteNeighborhoodsByID.contains(neighborhood.getID())) {
                favoriteList.add(neighborhood);
            }
        }
        return favoriteList;
    }

    /**
     * Load the given data into the ListView
     * @param data the list of neighborhoods to be loaded
     */
    private void loadListData(List<Neighborhood> data) {
        if (favoritesOnly) {
            List<Neighborhood> favorites = filterFavorites(data);
            adapter = new NeighborhoodAdapter(this.getActivity(),
                    android.R.layout.simple_list_item_1, favorites, true);
        } else {
            adapter = new NeighborhoodAdapter(this.getActivity(),
                    android.R.layout.simple_list_item_1, data, true);
        }
        neighborhoodList.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_neighborhood_catalog, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        neighborhoodList = (ListView) getActivity().findViewById(R.id.neighborhood_list);
        neighborhoodList.setOnItemClickListener(this);
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
     * When a neighborhood is clicked, it takes the user to the corresponding
     * alert forum page
     * @param adapterView the AdapterView that keeps track of the ListView elements
     * @param view the ListView for this class
     * @param position the position of the element clicked
     * @param id the id of the element clicked
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), AlertForumActivity.class);
        intent.putExtra("ALERT_TYPE", "Neighborhood");
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
