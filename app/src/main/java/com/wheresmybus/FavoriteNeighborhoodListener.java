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
 * A listener class that handles the event where a user selects or deselects a favorite neighborhood.
 */
public class FavoriteNeighborhoodListener implements View.OnClickListener {
    private int neighborhoodID;
    private boolean toggledOn;

    /**
     * Constructs a new FavoriteNeighborhoodListener.
     *
     * @param neighborhoodID the ID of the neighborhood that is being added to or removed from favorites
     */
    public FavoriteNeighborhoodListener(int neighborhoodID) {
        this.neighborhoodID = neighborhoodID;
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
        Set<Integer> favoritesSet = UserDataManager.getManager().getFavoriteNeighborhoodsByID();
        ImageButton button = (ImageButton) v.findViewById(R.id.star);
        if (toggledOn) {
            // go back to grey
            button.clearColorFilter();
            favoritesSet.remove(neighborhoodID);
        } else {
            button.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.yellow));
            // when go back to gray, use button.clearColorFilter();
            // to get the user ID: Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
            favoritesSet.add(neighborhoodID);
        }
        toggledOn = !toggledOn;
    }
}
