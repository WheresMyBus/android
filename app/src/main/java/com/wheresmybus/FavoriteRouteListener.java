package com.wheresmybus;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;

import java.util.Set;

import modules.UserDataManager;

/**
 * Created by lesli_000 on 11/13/2016.
 */

public class FavoriteRouteListener implements View.OnClickListener {
    private String routeID;
    private boolean toggledOn;

    public FavoriteRouteListener(String routeID) {
        this.routeID = routeID;
        this.toggledOn = false;
    }

    @Override
    public void onClick(View v) {
        // add to or remove from favorites
        // change color of star button
        ImageButton button = (ImageButton) v.findViewById(R.id.star);
        Set<String> favoritesSet = UserDataManager.getManager().getFavoriteRoutesByID();
        if (toggledOn) {
            button.clearColorFilter();
            favoritesSet.remove(routeID);
        } else {
            button.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.yellow));
            // when go back to gray, use button.clearColorFilter();
            // to get the user ID: Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
            favoritesSet.add(routeID);
        }
        toggledOn = !toggledOn;
    }
}
