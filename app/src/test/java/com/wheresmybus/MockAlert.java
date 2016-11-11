package com.wheresmybus;

import android.util.Pair;

import java.util.Date;

import modules.Alert;

/**
 * Created by Nick on 11/9/2016.
 * Mock implementation of the Alert abstract class for unit tests
 */

class MockAlert extends Alert {
    private MockAlert(String desc, Date date, String type, int creatorId, Pair<Double, Double> coords) {
        super(desc, date, type, creatorId, coords);
    }


    // creates a sample alert used by TestAlertBase
    static MockAlert makeSampleAlert() {
        return new MockAlert("Baz", new Date((long) 0), "Foo", 7, new MockPair());
    }
}
