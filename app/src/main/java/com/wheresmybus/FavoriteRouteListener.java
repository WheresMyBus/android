package com.wheresmybus;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by lesli_000 on 11/13/2016.
 */

public class FavoriteRouteListener implements View.OnClickListener {
    private String routeID;
    private CatalogActivity catalogActivity;
    private boolean toggledOn;

    public FavoriteRouteListener(String routeID, CatalogActivity catalogActivity) {
        this.catalogActivity = catalogActivity;
        this.routeID = routeID;
        this.toggledOn = false;
    }

    @Override
    public void onClick(View v) {
        // add to or remove from favorites
        // change color of star button
        ImageButton button = (ImageButton) v.findViewById(R.id.star);
        if (toggledOn) {
            button.clearColorFilter();
            catalogActivity.favoriteRoutesByID.remove(routeID);
        } else {
            button.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.yellow));
            // when go back to gray, use button.clearColorFilter();
            // to get the user ID: Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
            catalogActivity.favoriteRoutesByID.add(routeID);
        }
        toggledOn = !toggledOn;
    }
}
