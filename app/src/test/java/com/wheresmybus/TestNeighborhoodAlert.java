package com.wheresmybus;

import android.util.Pair;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import static junit.framework.Assert.*;

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

    @Test
    public void testSecondConstructor() {
        Alert a = new NeighborhoodAlert(1, 2, "3", "traffic", "lots", new Date(), 0, 5);
        assertTrue(a.getCreatorID().equals("3"));
        assertTrue(a.getId() == 2);
        assertTrue(a.getDescription().equals("lots"));
        assertTrue(a.getType().equals("traffic"));
        assertTrue(a.getDate() != null);
        assertTrue(a.getUpvotes() == 0);
        assertTrue(a.getDownvotes() == 5);
    }

    @Test
    public void testAffectedRoutes() {
        sampleNeighborhoodAlert().addAffectedRoute(new Route("1", "a", "2"));
        assertEquals(1, sampleNeighborhoodAlert().getRoutesAffected().size());
        assertTrue(sampleNeighborhoodAlert().getRoutesAffected().get(1).toString().equals("1 - a"));
    }
}
