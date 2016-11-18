package com.wheresmybus;

import android.util.Pair;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import modules.Bus;
import modules.Route;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by Nick on 11/9/2016.
 * Black Box tests for modules.Bus
 */

public class TestBus {
    private Bus sampleBus;

    @Before
    public void setUp() {
        sampleBus = new Bus(0.0, 1.0);
    }

    @Test
    public void testBasic() {
        assertTrue(sampleBus.getLat() == 0.0);
        assertTrue(sampleBus.getLon() == 1.0);
    }
}
