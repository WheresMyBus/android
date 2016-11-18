package com.wheresmybus;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Set;

import modules.UserDataManager;

public class MapActivity extends AppCompatActivity implements
        RouteMapFragment.OnFragmentInteractionListener,
        NeighborhoodMapFragment.OnFragmentInteractionListener {

    private ViewPager viewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private RouteMapFragment routeMapFragment;
    private NeighborhoodMapFragment neighborhoodMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // add fragments to the manager and grab references to them
        fragmentTransaction.add(R.id.route_map, new RouteMapFragment(), "ROUTE");

        routeMapFragment = (RouteMapFragment) fragmentManager.findFragmentById(R.id.route_map);
        routeMapFragment.getMapAsync(routeMapFragment);

        fragmentTransaction.add(R.id.neighborhood_map, new NeighborhoodMapFragment(), "NEIGHBORHOOD");

        neighborhoodMapFragment = (NeighborhoodMapFragment) fragmentManager
                .findFragmentById(R.id.neighborhood_map);
        neighborhoodMapFragment.getMapAsync(neighborhoodMapFragment);

        fragmentTransaction.commit();

        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(mSectionsPagerAdapter);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Gets the fragment at the given index
         * @param position The index of the fragment to display
         * @return The fragment (sub-page) to show
         */
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            if (position == 0) {
                return new RouteMapFragment();
            } else if (position == 1) {
                return new NeighborhoodMapFragment();
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        /**
         * Gets the title of the tab/page at the given index
         * @param position the index of the tab
         * @return the title of the given tab
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ROUTES";
                case 1:
                    return "NEIGHBORHOODS";
            }
            return null;
        }
    }
}
