package com.wheresmybus;

import android.util.Pair;

import org.junit.Test;

import java.util.Date;

import modules.Alert;
import modules.Route;
import modules.RouteAlert;

import static junit.framework.Assert.*;

/**
 * Created by Nick on 11/9/2016.
 * Black Box unit tests for modules.RouteAlert
 */

public class TestRouteAlert extends TestAlertBase<RouteAlert> {

    private RouteAlert sampleRouteAlert() {
        return new RouteAlert(new Route("106", "Downtown", "FooBar"),
                new Date((long) 0), "Foo", new Pair<>(1.0, 0.0), "Baz", "7");
    }

    @Override
    Alert createDefaultInstance() {
        return sampleRouteAlert();
    }

    @Override
    Alert createTypeFoo() {
        return sampleRouteAlert();
    }

    @Override
    Alert createDescBaz() {
        return sampleRouteAlert();
    }

    @Override
    Alert createDateZero() {
        return sampleRouteAlert();
    }

    @Override
    Alert createCreatorIdIsSeven() {
        return sampleRouteAlert();
    }

    @Override
    Alert createCoordinatesAreOneZero() {
        return sampleRouteAlert();
    }

    @Test
    public void testSecondConstructor() {
        RouteAlert a = new RouteAlert("1", 2, "3", "traffic", "lots", new Date(), 1, 4);
        assertTrue(a.getDate() != null);
        assertTrue(a.getDownvotes() == 4);
        assertTrue(a.getUpvotes() == 1);
        assertTrue(a.getType().equals("traffic"));
        assertTrue(a.getDescription().equals("lots"));
        assertTrue(a.getCreatorID().equals("3"));
        assertTrue(a.getId() == 2);
        assertTrue(a.getCoordinates() == null);
    }

    @Test
    public void testGetComments() {
        // TODO
    }
}
