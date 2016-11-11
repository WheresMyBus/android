package com.wheresmybus;

import android.util.Pair;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import modules.BusStop;
import modules.Route;
import static junit.framework.Assert.*;
/**
 * Unit tests for BusStop.java
 * Created by lidav on 11/11/2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class TestBusStop {
    /**
     * Tests that getCoordinates() stores and retrieves data correctly
     */
    @Test
    public void testBusStopGetCoords() {
        BusStop stop = new BusStop(new Pair<>(0.,1.), new HashSet<Route>());
        assertTrue(stop.getCoordinates() != null);
    }

    /**
     * Tests the getRoutes() stores and retrieves data correctly
     * Ensures RoutesSet cannot be changed by adding to the outside Set
     */
    @Test
    public void testGetRoutes() {
        Set<Route> routes = new HashSet<>();
        routes.add(new Route("1", "a", "b"));
        BusStop stop = new BusStop(new Pair<>(0.,1.), routes);
        assertTrue(stop.getRoutes() != null);
        assertTrue(stop.getRoutes().size() == 1);

        routes.add(new Route("2", "c", "d"));
        assertTrue(stop.getRoutes().size() == 1);
    }

    /**
     * Tests that setId sets properly once and returns true and changes id
     */
    @Test
    public void testSetId(){
        BusStop sampleBus = new BusStop(new Pair<>(0.,1.), new HashSet<Route>());
        assertEquals(-1, sampleBus.getId());
        assertTrue(sampleBus.setId(2));
        assertEquals(sampleBus.getId(), 2);
    }

    /**
     * Tests that IllegalArgumentException is thrown when coordinates is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullCoords() {
        BusStop stop = new BusStop(null, new HashSet<Route>());
    }

    /**
     * Tests that IllegalArgumentException is thrown when Set of routes is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullRoutes() {
        BusStop stop = new BusStop(new Pair<>(0.,1.), null);
    }

    /**
     * Test that id is stored properly and calling setId() twice will not affect id
     */
    @Test
    public void testGetId() {
        BusStop sampleBus = new BusStop(new Pair<>(0.,1.), new HashSet<Route>());
        boolean b = sampleBus.setId(2);
        assertTrue(b);
        assertTrue(sampleBus.getId() == 2);

        b = sampleBus.setId(3);
        assertFalse(b);
        assertTrue(sampleBus.getId() == 2);
    }
}
