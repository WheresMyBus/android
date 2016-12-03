package com.wheresmybus;

import modules.Comment;
import modules.VoteConfirmation;
import retrofit.Callback;
import android.util.Pair;

import java.util.Date;
import java.util.List;

import modules.Alert;

/**
 * Created by Nick on 11/9/2016.
 * Mock implementation of the Alert abstract class for unit tests
 */

class MockAlert extends Alert {
    private MockAlert(String desc, Date date, String type, String creatorId, Pair<Double, Double> coords) {
        super(desc, date, type, creatorId);
    }


    // creates a sample alert used by TestAlertBase
    static MockAlert makeSampleAlert() {
        return new MockAlert("Baz", new Date((long) 0), "Foo", "7", new Pair<>(1.,0.));
    }

    public void getComments(Callback<List<Comment>> callback) {
        return;
    }

    public void downvote(String userId, Callback<VoteConfirmation> callback) {
        System.out.println(downvotes);
        downvotes++;
        System.out.print(downvotes);
    }

    public void upvote(String userId, Callback<VoteConfirmation> callback) {
        this.upvotes++;
    }

    public void unvote(String userId, Callback<VoteConfirmation> callback) {}
}
