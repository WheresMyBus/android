package com.wheresmybus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import modules.UserDataManager;

/**
 * A class for the catalog screen, with tabs for routes and neighborhoods
 *
 * Created by bmartz on 11/1/2016.
 */
public class CatalogActivity extends AppCompatActivity implements BusRouteCatalogFragment.OnFragmentInteractionListener,
        NeighborhoodCatalogFragment.OnFragmentInteractionListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    // favorites only switch is on
    private boolean favoritesOnly;

    /**
     * Sets up the catalog screen with a routes and a neighborhoods tab
     * Each tab shows a list of the routes/neighborhoods in Seattle
     * The tab intially selected is determined by the button clicked to navigate here
     *
     * @param savedInstanceState previously saved state, or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // show the correct tab, depending on which button click sent here
        // routes if tabIndex = 0, neighborhoods if tabIndex = 1
        Intent intent = getIntent();
        int tabIndex = intent.getIntExtra("TAB_INDEX", 0);
        favoritesOnly = intent.getBooleanExtra("FAVORITES_ONLY", false);
        TabLayout.Tab tab = tabLayout.getTabAt(tabIndex);
        if (tab != null) {
            tab.select();
        }
    }

    /**
     * Get the UserDataManager according to the singleton pattern
     */
    @Override
    public void onStart() {
        super.onStart();
        UserDataManager.instantiateManager(this);
    }

    /**
     * Save the user data before stopping
     */
    @Override
    public void onStop() {
        super.onStop();
        UserDataManager.getManager().saveUserData(this);
    }

    /**
     * Creates an options menu.
     *
     * @param menu the menu to be inflated
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    /**
     * Handles the event when an option is selected from the options menu.
     *
     * @param item The selected item
     * @return false to allow normal menu processing to proceed, true to consume it here
     */
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

    /**
     * Converts the set of favorite route ids to an ArrayList
     * This method is necessary because while a set is a better implementation
     * of the collection of favorite routes, the types of arguments that may be
     * passed to a fragment's new instance method includes an ArrayList but not a set
     *
     * @param favoriteRoutes The set of favorite routes by id
     * @return An ArrayList of the ids of favorite routes
     */
    private ArrayList<String> routeSetToList(Set<String> favoriteRoutes) {
        ArrayList<String> favoriteRouteList = new ArrayList<String>();
        for (String favorite : favoriteRoutes) {
            favoriteRouteList.add(favorite);
        }
        return favoriteRouteList;
    }

    /**
     * Converts the set of favorite neighborhood ids to an ArrayList
     * This method is necessary because while a set is a better implementation
     * of the collection of favorite neighborhoods, the types of arguments that may be
     * passed to a fragment's new instance method includes an ArrayList but not a set
     *
     * @param favoriteNeighborhoods The set of favorite neighborhoods by id
     * @return An ArrayList of the ids of favorite neighborhoods
     */
    private ArrayList<Integer> neighborhoodSetToList(Set<Integer> favoriteNeighborhoods) {
        ArrayList<Integer> favoriteNeighborhoodList = new ArrayList<Integer>();
        for (Integer favorite : favoriteNeighborhoods) {
            favoriteNeighborhoodList.add(favorite);
        }
        return favoriteNeighborhoodList;
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
            UserDataManager manager = UserDataManager.getManager();
            Set<String> favoriteRoutesByID = manager.getFavoriteRoutesByID();
            Set<Integer> favoriteNeighborhoodsByID = manager.getFavoriteNeighborhoodsByID();
            // getItem is called to instantiate the fragment for the given page.
            if (position == 0) {
                return BusRouteCatalogFragment.newInstance(routeSetToList(favoriteRoutesByID),
                        favoritesOnly);
            } else if (position == 1) {
                return NeighborhoodCatalogFragment.newInstance(neighborhoodSetToList(favoriteNeighborhoodsByID),
                        favoritesOnly);
            } else {
                return null;
            }
        }

        /**
         * Gets the number of tabs
         *
         * @return the number of tabs, 2
         */
        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        /**
         * Gets the title of the tab/page at the given index
         *
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    // Methods for setting/unsetting favorites

}
