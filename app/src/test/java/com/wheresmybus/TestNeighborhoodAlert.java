package com.wheresmybus;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import modules.Alert;
import modules.Neighborhood;
import modules.NeighborhoodAlert;
import modules.Route;

/**
 * Created by Nick on 11/9/2016.
 * Black Box unit tests for modules.NeighborhoodAlert
 */

public class TestNeighborhoodAlert extends TestAlertBase<NeighborhoodAlert> {

    private static NeighborhoodAlert sampleNeighborhoodAlert() {
        return new NeighborhoodAlert(new Neighborhood(1, ""), new Date((long) 0),
                "Baz", "Foo", new Pair<>(1.0, 0.0), "7", new ArrayList<Route>());
    }

    @Override
    Alert createDefaultInstance() {
        return sampleNeighborhoodAlert();
    }

    @Override
    Alert createTypeFoo() {
        return sampleNeighborhoodAlert();
    }

    @Override
    Alert createDescBaz() {
        return sampleNeighborhoodAlert();
    }

    @Override
    Alert createDateZero() {
        return sampleNeighborhoodAlert();
    }

    @Override
    Alert createCreatorIdIsSeven() {
        return sampleNeighborhoodAlert();
    }

    @Override
    Alert createCoordinatesAreOneZero() {
        return sampleNeighborhoodAlert();
    }


}
