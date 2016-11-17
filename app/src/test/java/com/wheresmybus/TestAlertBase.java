package com.wheresmybus;

import android.util.Pair;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import modules.Alert;

/**
 * Created by nbissiri on 11/5/16
 * Black box test cases for modules.Alert
 * Must be extended with a test case defining the implementation
 * of Alert to use.
 */

public abstract class TestAlertBase<T extends Alert> {

    private Alert defaultAlert;
    private Alert typeIsFoo;
    private Alert descIsBaz;
    private Alert dateIsZero;
    private Alert creatorIdIsSeven;
    private Alert coordinatesAreOneZero;

    // call the default constructor
    abstract Alert createDefaultInstance();

    // construct with type = "Foo"
    abstract Alert createTypeFoo();

    // construct with description = "Baz"
    abstract Alert createDescBaz();

    // construct with date = new Date((long) 0)
    abstract Alert createDateZero();

    // construct with creator id = 7
    abstract Alert createCreatorIdIsSeven();

    // construct with coordinates = Pair(1.0, 0.0)
    abstract Alert createCoordinatesAreOneZero();

    @Before
    public void setUp() {
        defaultAlert = createDefaultInstance();
        typeIsFoo = createTypeFoo();
        descIsBaz = createDescBaz();
        dateIsZero = createDateZero();
        creatorIdIsSeven = createCreatorIdIsSeven();
        coordinatesAreOneZero = createCoordinatesAreOneZero();
    }

    @Test
    public void test_set_id() {
        assertEquals(-1, defaultAlert.getId());
        assertTrue(defaultAlert.setId(2));
        assertEquals(defaultAlert.getId(), 2);
    }

    @Test(expected=IllegalArgumentException.class)
    public void test_set_id_out_of_bounds(){
        defaultAlert.setId(0);
    }

    @Test
    public void test_set_id_already_set() {
        defaultAlert.setId(3);
        assertFalse(defaultAlert.setId(4));
        assertFalse(defaultAlert.setId(5));
        assertEquals(defaultAlert.getId(), 3);
    }

    @Test
    public void test_set_id_large_number() {
        assertTrue(defaultAlert.setId(Integer.MAX_VALUE));
        assertEquals(defaultAlert.getId(), Integer.MAX_VALUE);
    }

    @Test
    public void test_initial_upvotes() {
        assertEquals(0, defaultAlert.getUpvotes());
    }

    @Test
    public void test_initial_downvotes() {
        assertEquals(0, defaultAlert.getDownvotes());
    }

    @Test
    public void test_basic_upvote() {
        defaultAlert.upvote("", null);
        assertEquals(1, defaultAlert.getUpvotes());
    }

    @Test
    public void test_basic_downvote() {
        defaultAlert.downvote("", null);
        assertEquals(1, defaultAlert.getDownvotes());
    }

    @Test
    public void test_upvote_and_downvote() {
        defaultAlert.downvote("", null);
        defaultAlert.upvote("", null);
        defaultAlert.upvote("", null);
        defaultAlert.downvote("", null);
        defaultAlert.upvote("", null);
        assertEquals(2, defaultAlert.getDownvotes());
        assertEquals(3, defaultAlert.getUpvotes());
    }

    @Test
    public void test_get_type() {
        assertEquals(typeIsFoo.getType(), "Foo");
    }

    @Test
    public void test_get_description() {
        assertEquals(descIsBaz.getDescription(), "Baz");
    }

    @Test
    public void test_get_date() {
        assertEquals(new Date((long) 0), dateIsZero.getDate());
    }

    @Test
    public void test_get_creator_id() {
        assertEquals("7", creatorIdIsSeven.getCreatorID());
    }
}
