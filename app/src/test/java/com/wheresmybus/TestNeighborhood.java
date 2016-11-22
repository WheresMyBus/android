package com.wheresmybus;

import org.junit.Test;
import modules.*;
import static junit.framework.Assert.*;

/**
 * Unit tests for Neighborhood.java
 * Created by lidav on 11/11/2016.
 */

public class TestNeighborhood {
    /**
     * Tests that get methods properly store and retrieve data
     */
    @Test
    public void testGetMethods() {
        Neighborhood n = new Neighborhood(2, "H");
        assertTrue(n.getID() == 2);
        assertTrue(n.getName().equals("H"));
    }

    /**
     * Tests that IllegalArgumentException thrown when id < 1
     */
    @Test (expected = IllegalArgumentException.class)
    public void testBadId() {
        Neighborhood n = new Neighborhood(0, "h");
    }

    /**
     * Tests that IllegalArgumentException thrown when name is null
     */
    @Test (expected = IllegalArgumentException.class)
    public void testNullName() {
        Neighborhood n = new Neighborhood(1, null);
    }

    /**
     * Tests that the perimeter returns properly
     * NOT YET IMPLEMENTED, TEST SHOULD FAIL
     */
    /*@Test
    public void testGetPerimeter() {
        Neighborhood n = new Neighborhood(1, "h");
        assertTrue(n.getPerimeter() != null);
    }*/

    @Test
    public void testToString() {
        Neighborhood n = new Neighborhood(1, "h");
        assertTrue(n.getName().equals(n.toString()));
        assertTrue(n.toString().equals("h"));

        Neighborhood n2 = new Neighborhood(1, "n");
        assertTrue(n.compareTo(n2) < 0);
    }
}
