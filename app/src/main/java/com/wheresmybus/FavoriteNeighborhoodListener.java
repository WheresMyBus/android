package com.wheresmybus;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;

import java.util.Set;

import modules.UserDataManager;

/**
 * Created by lesli_000 on 11/13/2016.
 */

public class FavoriteNeighborhoodListener implements View.OnClickListener {
    private int neighborhoodID;
    private boolean toggledOn;

    public FavoriteNeighborhoodListener(int neighborhoodID) {
        this.neighborhoodID = neighborhoodID;
        this.toggledOn = false;
    }

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
