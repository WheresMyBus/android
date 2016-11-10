package com.wheresmybus;

import android.util.Pair;

import org.junit.Before;
import org.junit.Test;
import static junit.framework.Assert.*;

import java.util.Date;
import modules.*;

/**
 * Unit tests for Comment.java
 * Created by lidav on 11/8/2016.
 */

public class TestComment {
    private Comment comment;
    private Alert alert;

    @Before
    public void setup() {
        alert = new RouteAlert(new Route("12", "a", "b"), new Date(), "b", new Pair<>(0.0,1.0), "c", 2);
        comment = new Comment("d", 1, alert);
    }

    /**
     * upvote 3 times, upvote 3 times, check that votes were counted correctly
     */
    @Test
    public void testVote() {
        comment.upvote();
        comment.upvote();
        comment.downvote();
        comment.downvote();
        comment.upvote();
        comment.downvote();
        assertEquals(comment.getDownvotes(), 3);
        assertEquals(comment.getUpvotes(), 3);
    }

    /**
     * Check that data is stored correctly and retrieved correctly
     */
    @Test
    public void testGetData() {
        assertTrue(alert.equals(comment.getAlert()));
        assertEquals(1, comment.getCreatorID());
        assertTrue(comment.getData().equals("d"));
    }

    /**
     * Check that IllegalArgumentException thrown when null Alert is passed
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullAlert() {
        Comment comment2 = new Comment("e", 2, null);
    }

    /**
     * Check that
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadCreatorId() {
        Comment comment3 = new Comment("f", 0, alert);
    }

    @Test
    public void testSetId() {
        boolean b = comment.setId(10);
        assertEquals(10, comment.getId());
        assertTrue(b);
        b = comment.setId(20);
        assertFalse(b);
        assertEquals(10, comment.getId());
    }
}
