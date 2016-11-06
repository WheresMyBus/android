package com.wheresmybus;

import android.util.Pair;

import java.util.Date;

import modules.Alert;

/**
 * Created by Nick on 11/5/2016
 * Tests a Mock implementor of modules.Alert
 */

public class TestAlert extends TestAlertBase {
    private class MockAlert extends Alert{
        MockAlert(String desc, Date date, String type, int creatorId, Pair<Double, Double> coords) {
            super(desc, date, type, creatorId, coords);
        }
        MockAlert() {
            super();
        }
    }

    private MockAlert makeSampleAlert() {
        return new MockAlert("Baz", new Date((long) 0), "Foo", 7, new Pair<>(1.0, 0.0));
    }

    @Override
    Alert createDefaultInstance() {
        return new MockAlert();
    }

    @Override
    Alert createTypeFoo() {
        return makeSampleAlert();
    }

    @Override
    Alert createDescBaz() {
        return makeSampleAlert();
    }

    @Override
    Alert createDateZero() {
        return makeSampleAlert();
    }

    @Override
    Alert createCreatorIdIsSeven() {
        return makeSampleAlert();
    }

    @Override
    Alert createCoordinatesAreOneZero() {
        return makeSampleAlert();
    }
}
