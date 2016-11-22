package com.wheresmybus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * The activity that displays different ways users can search for a route forum and a button to
 * submit a route alert.
 */
public class RouteMainActivity extends AppCompatActivity {

    /**
     * Part of the call structure that displays this activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_main);
    }

    /**
     * Switches the activity displayed from the current activity to the catalog of bus route forums.
     *
     * @param v the current view
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
     * @param v the current view
     */
    public void switchToRouteMap(View v) {
        Intent intent = new Intent(this, SearchRouteMapActivity.class);
        startActivity(intent);
    }

    /**
     * Switches the activity displayed from the current activity to the catalog page for routes with
     * the favorites switch turned on.
     *
     * @param v the current view
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
     * @param v the current view
     */
    public void switchToSubmitAlert(View v) {
        Intent intent = new Intent(this, SubmitAlertActivity.class);
        intent.putExtra("IS_ROUTE", true);
        startActivity(intent);
    }
}