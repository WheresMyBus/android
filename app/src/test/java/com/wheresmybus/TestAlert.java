package com.wheresmybus;

import android.util.Pair;

import java.util.Date;

import modules.Alert;

import com.wheresmybus.MockAlert;
import static com.wheresmybus.MockAlert.makeSampleAlert;

/**
 * Created by Nick on 11/5/2016
 * Tests a Mock implementor of modules.Alert
 */

public class TestAlert extends TestAlertBase {

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
