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
        Route r = new Route("12", "Seattle", "a");
        assertEquals("12", r.getNumber());
        assertTrue(r.getName().equals("Seattle"));
    }

    /**
     * Check that IllegalArgumentException is thrown when name is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        Route m = new Route("12", null, null);
    }
    /**
     * Check that IllegalArgumentException is thrown when number is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullNumber() {
        Route m = new Route(null, "", "");
    }
    /**
     * Check that IllegalArgumentException is thrown when id is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullId() {
        Route m = new Route("", "", null);
    }


    /**
     * Check the toString() produces the correct result
     */
    @Test
    public void testToString() {
        Route r = new Route("556", "Northgate", "1");
        assertTrue(r.toString().equals("[556] Northgate"));
    }

    /**
     *
     */
    @Test
    public void testEquals() {
        Route r1 = new Route("1", "2", "3");
        Route r2 = new Route("1", "2", "3");
        Route notEqual = new Route("2", "", "");
        assertTrue(r1.equals(r2));
        assertFalse(r2.equals(notEqual));
    }

    @Test
    public void testCompareTo() {
        Route r1 = new Route("1", "", "");
        Route r2 = new Route("2", "", "");
        Route a1 = new Route("1", "a", "b");
        assertTrue(r1.compareTo(r2) < 0);
        assertTrue(r1.compareTo(a1) == 0);
        assertTrue(r2.compareTo(r1) > 0);
    }

}
