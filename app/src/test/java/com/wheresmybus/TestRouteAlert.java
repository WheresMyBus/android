package com.wheresmybus;

import android.util.Pair;

import java.util.Date;

import modules.Alert;
import modules.Route;
import modules.RouteAlert;

/**
 * Created by Nick on 11/9/2016.
 * Black Box unit tests for modules.RouteAlert
 */

public class TestRouteAlert extends TestAlertBase<RouteAlert> {

    private RouteAlert sampleRouteAlert() {
        return new RouteAlert(new Route("106", "Downtown", "FooBar"), new Date((long) 0), "Foo", new Pair<>(1.0, 0.0), "Baz", 7);
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
}
