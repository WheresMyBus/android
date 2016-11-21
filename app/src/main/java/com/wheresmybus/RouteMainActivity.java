package com.wheresmybus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RouteMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_main);
    }

    /**
     * Switches the activity displayed from the current activity to the catalog of bus route forums.
     *
     * @param v the button clicked
     */
    public void switchToRouteCatalog(View v) {
        Intent intent = new Intent(this, CatalogActivity.class);
        intent.putExtra("TAB_INDEX", 0);
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
        // TODO: implement this method
    }

    /**
     * Switches the activity displayed from the current activity to the activity where users can
     * submit an alert.
     *
     * @param v the button clicked
     */
    public void switchToSubmitAlert(View v) {
        Intent intent = new Intent(this, SubmitAlertActivity.class);
        startActivity(intent);
    }
}
