package com.wheresmybus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import modules.UserDataManager;

/**
 * The activity associated with the main screen of the app (the home screen).
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Part of the call structure that displays this activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onStart() {
        super.onStart();
        UserDataManager.instantiateManager(this);
    }

    /**
     * Creates an options menu.
     *
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handles the event when an option is selected from the options menu.
     *
     * @param item
     * @return
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
     * Switches the activity displayed from the current activity to the catalog of bus route forums.
     *
     * @param v
     */
    public void switchToRouteCatalog(View v) {
        Intent intent = new Intent(this, CatalogActivity.class);
        intent.putExtra("TAB_INDEX", 0);
        startActivity(intent);
    }

    /**
     * Switches the activity displayed from the current activity to the catalog of neighborhood
     * forums.
     *
     * @param v
     */
    public void switchToNeighborhoodCatalog(View v) {
        Intent intent = new Intent(this, CatalogActivity.class);
        intent.putExtra("TAB_INDEX", 1);
        startActivity(intent);
    }

    /**
     * Switches the activity displayed from the current activity to the activity where users can
     * submit an alert.
     *
     * @param v
     */
    public void switchToSubmitAlert(View v) {
        Intent intent = new Intent(this, SubmitAlertActivity.class);
        startActivity(intent);
    }

    public void switchToMap(View view) {
        Intent intent = new Intent(this, RouteMapActivity.class);
        startActivity(intent);
    }
}
