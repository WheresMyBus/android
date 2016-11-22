package com.wheresmybus;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;

import java.util.Set;

import modules.UserDataManager;

/**
 * Created by lesli_000 on 11/13/2016.
 */

/**
 * A listener class that handles the event where a user selects or deselects a favorite route.
 */
public class FavoriteRouteListener implements View.OnClickListener {
    private String routeID;
    private boolean toggledOn;

    /**
     * Constructs a new FavoriteRouteListener.
     *
     * @param routeID the ID for the route associated with this listener
     */
    public FavoriteRouteListener(String routeID) {
        this.routeID = routeID;
        this.toggledOn = false;
    }

    /**
     * Adds or removes the neighborhood associated with the given button view to or from the user's
     * list of favorites as appropriate.
     *
     * If the button was off when the user clicked it, the button appears gray and then turns yellow.
     * This means the neighborhood is being added to the user's list of favorite neighborhoods.
     * Otherwise, if the button was on when the user clicked it, the button appears yellow and then
     * turns gray. This means the neighborhood is being removed from the user's list of favorites.
     *
     * @param v the button clicked
     */
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
