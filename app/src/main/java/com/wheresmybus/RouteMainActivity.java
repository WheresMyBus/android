package com.wheresmybus;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * The activity that displays different ways users can search for a route forum and a button to
 * submit a route alert.
 */
public class RouteMainActivity extends AppCompatActivity {

    /**
     * Part of the call structure that displays this activity.
     *
     * @param savedInstanceState previously saved state, or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if (id == R.id.action_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            return true;
        } else if (id == R.id.action_help) {
            Uri uri = Uri.parse("https://github.com/WheresMyBus/android/wiki/User-Documentation");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Switches the activity displayed from the current activity to the catalog of bus route forums.
     *
     * @param v the button clicked
     */
    public void switchToRouteCatalog(View v) {
        Intent intent = new Intent(this, CatalogActivity.class);
        intent.putExtra("TAB_INDEX", 0);
        intent.putExtra("FAVORITES_ONLY", false);
        startActivity(intent);
    }

    /**
     * Switches the activity displayed from the current activity to the map where users can search
     * for a route forum by viewing nearby bus stops.
     *
     * @param v the button clicked
     */
    public void switchToRouteMap(View v) {
        Intent intent = new Intent(this, SearchRouteMapActivity.class);
        startActivity(intent);
    }

    /**
     * Switches the activity displayed from the current activity to the catalog page for routes with
     * the favorites switch turned on.
     *
     * @param v the button clicked
     */
    public void switchToRouteFavorites(View v) {
        Intent intent = new Intent(this, CatalogActivity.class);
        intent.putExtra("TAB_INDEX", 0);
        intent.putExtra("FAVORITES_ONLY", true);
        startActivity(intent);
    }

    /**
     * Switches the activity displayed from the current activity to the activity where users can
     * submit an alert.
     *
     * @param v the button clicked
     */
    public void switchToSubmitAlert(View v) {
        Intent intent = new Intent(this, SubmitAlertActivity.class);
        intent.putExtra("IS_ROUTE", true);
        startActivity(intent);
    }
}
