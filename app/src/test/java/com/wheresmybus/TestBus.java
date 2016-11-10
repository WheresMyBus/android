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
        sampleBus = new Bus(new Route(1, ""), new Pair<>(0.0, 1.0));
    }

    @Test
    public void test_set_id() {
        assertEquals(-1, sampleBus.getId());
        assertTrue(sampleBus.setId(2));
        assertEquals(sampleBus.getId(), 2);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_set_id_out_of_bounds(){
        sampleBus.setId(0);
    }

    @Test
    public void test_set_id_already_set() {
        sampleBus.setId(3);
        assertFalse(sampleBus.setId(4));
        assertFalse(sampleBus.setId(5));
        assertEquals(sampleBus.getId(), 3);
    }

    @Test
    public void test_set_id_large_number() {
        assertTrue(sampleBus.setId(Integer.MAX_VALUE));
        assertEquals(sampleBus.getId(), Integer.MAX_VALUE);
    }

}
