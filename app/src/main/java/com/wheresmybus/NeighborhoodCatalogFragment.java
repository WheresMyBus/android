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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ListView neighborhoodList;

    private OnFragmentInteractionListener mListener;

    public NeighborhoodCatalogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NeighborhoodCatalogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NeighborhoodCatalogFragment newInstance(String param1, String param2) {
        NeighborhoodCatalogFragment fragment = new NeighborhoodCatalogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
     * Load the given data into the ListView
     * @param data the list of neighborhoods to be loaded
     */
    private void loadListData(List<Neighborhood> data) {
        NeighborhoodAdapter adapter = new NeighborhoodAdapter(this.getActivity(),
                android.R.layout.simple_list_item_1, data);
        neighborhoodList.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
