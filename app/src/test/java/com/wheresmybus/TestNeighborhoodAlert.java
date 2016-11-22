package com.wheresmybus;

import android.util.Pair;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertEquals;

import modules.Alert;
import modules.Comment;
import modules.Neighborhood;
import modules.NeighborhoodAlert;
import modules.Route;
import modules.VoteConfirmation;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Nick on 11/9/2016.
 * Black Box unit tests for modules.NeighborhoodAlert
 */

public class TestNeighborhoodAlert extends TestAlertBase<NeighborhoodAlert> {

    private static NeighborhoodAlert sampleNeighborhoodAlert() {
        return new NeighborhoodAlert(new Neighborhood(1, ""), new Date((long) 0),
                "Baz", "Foo", new Pair<>(1.0, 0.0), "7", new ArrayList<Route>());
    }

    @Override
    Alert createDefaultInstance() {
        return sampleNeighborhoodAlert();
    }

    @Override
    Alert createTypeFoo() {
        return sampleNeighborhoodAlert();
    }

    @Override
    Alert createDescBaz() {
        return sampleNeighborhoodAlert();
    }

    @Override
    Alert createDateZero() {
        return sampleNeighborhoodAlert();
    }

    @Override
    Alert createCreatorIdIsSeven() {
        return sampleNeighborhoodAlert();
    }

    @Override
    Alert createCoordinatesAreOneZero() {
        return sampleNeighborhoodAlert();
    }

    @Test
    public void testSecondConstructor() {
        Alert a = new NeighborhoodAlert(1, 2, "3", "traffic", "lots", new Date(), 0, 5);
        assertTrue(a.getCreatorID().equals("3"));
        assertTrue(a.getId() == 2);
        assertTrue(a.getDescription().equals("lots"));
        assertTrue(a.getType().equals("traffic"));
        assertTrue(a.getDate() != null);
        assertTrue(a.getUpvotes() == 0);
        assertTrue(a.getDownvotes() == 5);
    }

    @Test
    public void testAffectedRoutes() {
        sampleNeighborhoodAlert().addAffectedRoute(new Route("1", "a", "2"));
        assertTrue(sampleNeighborhoodAlert().getRoutesAffected() != null);
    }


    @Test
    public void test_initial_upvotes() {
        assertEquals(0, sampleNeighborhoodAlert().getUpvotes());
    }

    @Test
    public void test_initial_downvotes() {
        assertEquals(0, sampleNeighborhoodAlert().getDownvotes());
    }


    @Test
    public void test_upvote_and_downvote() {
        sampleNeighborhoodAlert().downvote("", new Callback<VoteConfirmation>() {
            @Override
            public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
                assertEquals(1, sampleNeighborhoodAlert().getDownvotes());
            }
            @Override
            public void onFailure(Throwable t) {
            }
        });
        sampleNeighborhoodAlert().upvote("", new Callback<VoteConfirmation>() {
            @Override
            public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
                assertEquals(1, sampleNeighborhoodAlert().getUpvotes());
            }
            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    @Test
    public void testGetNeighborhood() {
        Neighborhood n = sampleNeighborhoodAlert().getNeighborhood();
        assertFalse(n == null);
        assertTrue(n.getName().equals(""));
        assertTrue(n.getID() == 1);
    }

    @Test
    public void testGetComments() {
        sampleNeighborhoodAlert().getComments(new Callback<List<Comment>>() {
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

    @Test
    public void testUnvote() {
        sampleNeighborhoodAlert().upvote("", new Callback<VoteConfirmation>() {
            @Override
            public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
                assertEquals(1, sampleNeighborhoodAlert().getUpvotes());
            }
            @Override
            public void onFailure(Throwable t) {
            }
        });
        sampleNeighborhoodAlert().unvote("", new Callback<VoteConfirmation>() {
            @Override
            public void onResponse(Response<VoteConfirmation> response, Retrofit retrofit) {
                assertEquals(0, sampleNeighborhoodAlert().getUpvotes());
            }
            @Override
            public void onFailure(Throwable t) {
            }
        });
    }
}
