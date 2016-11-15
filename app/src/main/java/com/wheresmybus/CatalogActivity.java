package com.wheresmybus;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
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

import java.util.HashSet;
import java.util.Set;

/**
 * A class for the catalog screen, with tabs for routes and neighborhoods
 */
public class CatalogActivity extends AppCompatActivity implements BusRouteCatalogFragment.OnFragmentInteractionListener,
        NeighborhoodCatalogFragment.OnFragmentInteractionListener{

    /**
     * The set of route IDs favorited by the user.
     * Saved and restored by onSaveInstanceState and onRestoreInstanceState
     */
    public Set<String> favoriteRoutesByID;
    // TODO: add more data sets
    public Set<Integer> favoriteNeighborhoodsByID;

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

    /**
     * Sets up the catalog screen with a routes and a neighborhoods tab
     * Each tab shows a list of the routes/neighborhoods in Seattle
     * The tab intially selected is determined by the button clicked to navigate here
     * @param savedInstanceState
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

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Intent intent = getIntent();
        int tabIndex = intent.getIntExtra("TAB_INDEX", 0);
        TabLayout.Tab tab = tabLayout.getTabAt(tabIndex);
        if (tab != null) {
            tab.select();
        }

        // Initialize user data sets: favorites sets, like sets, dislike sets
        favoriteRoutesByID = new HashSet<String>();
        favoriteNeighborhoodsByID = new HashSet<Integer>();
        // TODO: add other data sets

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // save favorite routes
        CharSequence[] favoriteRoutesHolder = new CharSequence[favoriteRoutesByID.size()];
        int i = 0;
        for (String s : favoriteRoutesByID) {
            favoriteRoutesHolder[i] = s;
            i++;
        }
        savedInstanceState.putCharSequenceArray("FavoriteRoutes", favoriteRoutesHolder);
        // TODO: save other kinds of data
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // restore favorite routes
        for (CharSequence cs : savedInstanceState.getCharSequenceArray("FavoriteRoutes")) {
            favoriteRoutesByID.add(cs.toString());
        }
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
                return new BusRouteCatalogFragment();
            } else if (position == 1) {
                return new NeighborhoodCatalogFragment();
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    // Methods for setting/unsetting favorites

}
