package com.wheresmybus;

import android.util.Pair;

import org.junit.Test;

import java.util.Date;
import java.util.List;

import modules.Alert;
import modules.Comment;
import modules.Route;
import modules.RouteAlert;
import modules.VoteConfirmation;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by Nick on 11/9/2016.
 * Black Box unit tests for modules.RouteAlert
 */

public class TestRouteAlert extends TestAlertBase<RouteAlert> {

    private RouteAlert sampleRouteAlert() {
        return new RouteAlert(new Route("106", "Downtown", "FooBar"),
                new Date((long) 0), "Foo", new Pair<>(1.0, 0.0), "Baz", "7");
    }

    @Override
    Alert createDefaultInstance() {
        return sampleRouteAlert();
    }

    @Override
    Alert createTypeFoo() {
        return sampleRouteAlert();
    }

    @Override
    Alert createDescBaz() {
        return sampleRouteAlert();
    }

    @Override
    Alert createDateZero() {
        return sampleRouteAlert();
    }

    @Override
    Alert createCreatorIdIsSeven() {
        return sampleRouteAlert();
    }

    @Override
    Alert createCoordinatesAreOneZero() {
        return sampleRouteAlert();
    }

    @Test
    public void testSecondConstructor() {
        RouteAlert a = new RouteAlert("1", 2, "3", "traffic", "lots", new Date(), 1, 4);
        assertTrue(a.getDate() != null);
        assertTrue(a.getDownvotes() == 4);
        assertTrue(a.getUpvotes() == 1);
        assertTrue(a.getType().equals("traffic"));
        assertTrue(a.getDescription().equals("lots"));
        assertTrue(a.getCreatorID().equals("3"));
        assertTrue(a.getId() == 2);
    }

    @Test
    public void test_initial_upvotes() {
        assertEquals(0, sampleRouteAlert().getUpvotes());
    }

    @Test
    public void test_initial_downvotes() {
        assertEquals(0, sampleRouteAlert().getDownvotes());
    }


    @Test
    public void test_upvote_and_downvote() {
        sampleRouteAlert().downvote("", new Callback<VoteConfirmation>() {
            @Override
            public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
                assertEquals(1, sampleRouteAlert().getDownvotes());
            }
            @Override
            public void onFailure(Throwable t) {
            }
        });
        sampleRouteAlert().upvote("", new Callback<VoteConfirmation>() {
            @Override
            public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
                assertEquals(1, sampleRouteAlert().getUpvotes());
            }
            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    @Test
    public void testGetComments() {
        sampleRouteAlert().getComments(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Response<List<Comment>> response, Retrofit retrofit) {
                assertTrue(response != null);
                assertTrue(response.body().size() == 0);
            }

            @Override
            public void onFailure(Throwable t) {
                assertTrue(false);
            }
        });
    }

}
