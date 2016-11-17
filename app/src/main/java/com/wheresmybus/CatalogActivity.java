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

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Intent intent = getIntent();
        int tabIndex = intent.getIntExtra("TAB_INDEX", 0);
        TabLayout.Tab tab = tabLayout.getTabAt(tabIndex);
        if (tab != null) {
            tab.select();
        }

        final Switch favoriteSwitch =  (Switch) findViewById(R.id.favoriteSwitch);
        favoriteSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show only favorites when checked
            }
        });
    }

    @Override
    public void onStart() {
        // Initialize user data sets: favorites sets, like sets, dislike sets
        super.onStart();
        loadSavedData();
    }
    @Override
    public void onStop() {
        super.onStop();
        storeSavedData();
    }

    private void loadSavedData() {
        File fileDir = getFilesDir();
        File[] files = fileDir.listFiles();
        favoriteRoutesByID = null;
        favoriteNeighborhoodsByID = null;

        for (File f : files) {
            try {
                //if (f.isFile()) {
                    FileInputStream fileIn = new FileInputStream(f);
                    ObjectInputStream objIn = new ObjectInputStream(fileIn);
                    String name = f.getName();
                    String path = fileDir.getPath();
                    if (name.equals("FavoriteRoutes")) {
                        favoriteRoutesByID = (Set<String>) objIn.readObject();
                    } else if (name.equals("FavoriteNeighborhoods")) {
                        favoriteNeighborhoodsByID = (Set<Integer>) objIn.readObject();
                    }
                //}
            } catch (IOException e) {
                System.err.println("Error loading user data: IO failed, filname: " + f.getName());
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("Error loading user data: class not found, filename: " + f.getName());
                e.printStackTrace();
            }
        }

        if (favoriteNeighborhoodsByID == null) {
            favoriteNeighborhoodsByID = new HashSet<Integer>();
        }
        if (favoriteRoutesByID == null) {
            favoriteRoutesByID = new HashSet<String>();
        }

    }
    private void storeSavedData() {
        FileOutputStream outputStream;
        ObjectOutputStream objOutputStream;
        try {
            outputStream = openFileOutput("FavoriteRoutes", Context.MODE_PRIVATE);
            objOutputStream = new ObjectOutputStream(outputStream);
            objOutputStream.writeObject(favoriteRoutesByID);
            objOutputStream.close();
            outputStream.close();
            outputStream = openFileOutput("FavoriteNeighborhoods", Context.MODE_PRIVATE);
            objOutputStream = new ObjectOutputStream(outputStream);
            objOutputStream.writeObject(favoriteNeighborhoodsByID);
            objOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            System.err.println("Error storing user data");
            e.printStackTrace();
        }
    }
    /*
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // save favorite routes
        CharSequence[] favoriteRoutesHolder = new CharSequence[favoriteRoutesByID.size()];
        CharSequence[] favoriteNeighborhoodsHolder = new CharSequence[favoriteNeighborhoodsByID.size()];

        int i = 0;
        for (String id : favoriteRoutesByID) {
            favoriteRoutesHolder[i] = id;
            i++;
        }

        int j = 0;
        for (int id : favoriteNeighborhoodsByID) {
            favoriteNeighborhoodsHolder[j] = String.valueOf(id);
            j++;
        }

        savedInstanceState.putCharSequenceArray("FavoriteRoutes", favoriteRoutesHolder);
        savedInstanceState.putCharSequenceArray("FavoriteNeighborhoods", favoriteNeighborhoodsHolder);
        // TODO: save other kinds of data
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // restore favorite routes
        for (CharSequence cs : savedInstanceState.getCharSequenceArray("FavoriteRoutes")) {
            favoriteRoutesByID.add(cs.toString());
        }

        // restore favorite neighborhoods
        for (CharSequence cs : savedInstanceState.getCharSequenceArray("FavoriteNeighborhoods")) {
            favoriteNeighborhoodsByID.add(Integer.parseInt(cs.toString()));
        }
    }
    */
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
