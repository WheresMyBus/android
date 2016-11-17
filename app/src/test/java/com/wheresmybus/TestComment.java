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
        comment = new Comment("d", "1", new Date());
    }

    @Test
    public void testSecondConstructor(){
        Comment c = new Comment(1, "2", "hi", 2, 0, new Date());
        assertTrue(c.getId() == 1);
        assertTrue(c.getCreatorID().equals("2"));
        assertTrue(c.getData().equals("hi"));
        assertTrue(c.getUpvotes() == 2);
        assertTrue(c.getDownvotes() == 0);
        assertTrue(c.getDate() != null);
    }

    /**
     * upvote 3 times, upvote 3 times, check that votes were counted correctly
     */
    @Test
    public void testVote() {
        comment.upvote("0", null);
        comment.upvote("0", null);
        comment.downvote("0", null);
        comment.downvote("0", null);
        comment.upvote("0", null);
        comment.downvote("0", null);
        assertEquals(comment.getDownvotes(), 3);
        assertEquals(comment.getUpvotes(), 3);
    }

    /**
     * Check that data is stored correctly and retrieved correctly
     */
    @Test
    public void testGetData() {
        assertEquals("1", comment.getCreatorID());
        assertTrue(comment.getData().equals("d"));
    }

    /**
     * Check that
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadCreatorId() {
        Comment comment3 = new Comment("f", null, new Date());
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
