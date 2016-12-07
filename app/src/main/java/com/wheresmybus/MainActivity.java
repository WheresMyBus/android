package com.wheresmybus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import modules.UserDataManager;


public class MainActivity extends AppCompatActivity {

    /**
     * Sets up the main screen upon the activity's creation
     * which includes setting up the navigation menu
     *
     * @param savedInstanceState previously saved state, or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
     * Creates an options menu.
     *
     * @param menu the menu to be inflated
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
        if (id == R.id.action_help) {
            Uri uri = Uri.parse("https://github.com/WheresMyBus/android/wiki/User-Documentation");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Switches the activity displayed from the current activity to the catalog
     * of neighborhood forums.
     *
     * @param v the button clicked
     */
    public void switchToNeighborhoodCatalog(View v) {
        Intent intent = new Intent(this, CatalogActivity.class);
        intent.putExtra("TAB_INDEX", 1);
        intent.putExtra("FAVORITES_ONLY", false);
        startActivity(intent);
    }

    /**
     * Switches the activity displayed to the route catalog with the favorites
     * switch on
     *
     * @param v the button clicked
     */
    public void viewFavorites(View v) {
        Intent intent = new Intent(this, CatalogActivity.class);
        intent.putExtra("TAB_INDEX", 0);
        intent.putExtra("FAVORITES_ONLY", true);
        startActivity(intent);
    }

    /**
     * Switches the activity displayed from the current activity to the activity
     * where users can submit an alert.
     *
     * @param v the button clicked
     */
    public void switchToSubmitAlert(View v) {
        Intent intent = new Intent(this, SubmitAlertActivity.class);
        startActivity(intent);
    }

    /**
     * Switches the activity displayed from the current activity to the activity
     * where users can indicate how they wish to search for a route forum.
     *
     * @param v the button clicked
     */
    public void switchToRouteMain(View v) {
        Intent intent = new Intent(this, RouteMainActivity.class);
        startActivity(intent);
    }
}
