package com.wheresmybus;

import android.util.Pair;

import org.junit.Before;
import org.junit.Test;
import static junit.framework.Assert.*;

import java.util.Date;
import modules.*;

/**
 * Tests for Route.java
 * Created by lidav on 11/9/2016.
 */

public class TestRoute {
    /**
     * Check that data is stored and retrieved correctly
     */
    @Test
    public void testGetData() {
        Route r = new Route(12, "Seattle");
        assertEquals(12, r.getNumber());
        assertTrue(r.getName().equals("Seattle"));
    }

    /**
     * Check that IllegalArgumentException is thrown when name is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        Route m = new Route(12, null);
    }

    /**
     * Check the IllegalArgumentException is thrown when number < 1
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadNumber() {
        Route a = new Route(0, null);
    }

    /**
     * Check the toString() produces the correct result
     */
    @Test
    public void testToString() {
        Route r = new Route(556, "Northgate");
        assertTrue(r.toString().equals("556 Northgate"));
    }

}
