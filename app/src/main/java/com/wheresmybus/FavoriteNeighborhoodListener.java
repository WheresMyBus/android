package com.wheresmybus;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by lesli_000 on 11/13/2016.
 */

public class FavoriteNeighborhoodListener implements View.OnClickListener {
    private int neighborhoodID;

    public FavoriteNeighborhoodListener(int neighborhoodID) {
        this.neighborhoodID = neighborhoodID;
    }

    @Override
    public void onClick(View v) {
        // add to or remove from favorites
        // change color of star button
        ImageButton button = (ImageButton) v.findViewById(R.id.star);
        button.setColorFilter(ContextCompat.getColor(v.getContext(), R.color.yellow));
        // when go back to gray, use button.clearColorFilter();

        // to get the user ID: Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
        ((CatalogActivity) v.getContext()).favoriteNeighborhoodsByID.add(neighborhoodID);
    }
}
