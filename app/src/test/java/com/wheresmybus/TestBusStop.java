package com.wheresmybus;

import android.util.Pair;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import modules.BusStop;
import modules.Route;
import static junit.framework.Assert.*;
/**
 * Unit tests for BusStop.java
 * Created by lidav on 11/11/2016.
 */

public class TestBusStop {
    /**
     * Tests that getCoordinates() stores and retrieves data correctly
     */
    @Test
    public void testBusStopGetCoords() {
        BusStop stop = new BusStop("s", 0.0,1,"b", "a", new ArrayList<Route>());
        assertTrue(stop.getLon() == 1.0);
        assertTrue(stop.getLat() == 0.0);
    }

    /**
     * Tests the getRoutes() stores and retrieves data correctly
     * Ensures RoutesSet cannot be changed by adding to the outside Set
     */
    @Test
    public void testGetRoutes() {
        Set<Route> routes = new HashSet<>();
        routes.add(new Route("1", "a", "b"));
        BusStop stop = new BusStop("s", 0.0,1,"b", "a", new ArrayList<Route>());

        assertTrue(stop.getRoutes() != null);
        assertTrue(stop.getRoutes().size() == 0);

        routes.add(new Route("2", "c", "d"));
        assertTrue(stop.getRoutes().size() == 0);
    }

    /**
     * Tests that IllegalArgumentException is thrown when id is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullId() {
        BusStop stop = new BusStop(null, 0.0,1,"b", "a", new ArrayList<Route>());
    }

    /**
     * Tests that IllegalArgumentException is thrown when Set of routes is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullRoutes() {
        BusStop stop = new BusStop("s", 0.0,1,"b", "a",null);
    }

    /**
     * Tests that IllegalArgumentException is thrown when Set of routes is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullDirection() {
        BusStop stop = new BusStop("s", 0.0,1,null, "a",null);
    }

    /**
     * Tests that IllegalArgumentException is thrown when Set of routes is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        BusStop stop = new BusStop("s", 0.0,1,"b",null,null);
    }
}
