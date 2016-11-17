package com.wheresmybus;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import controllers.WMBController;
import modules.Comment;
import modules.Neighborhood;
import modules.NeighborhoodAlert;
import modules.VoteConfirmation;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;

/**
 * Created by gunter on 11/11/16.
 */
@RunWith(AndroidJUnit4.class)
public class MockedInstrumentedTest {
    private MockWebServer server;
    private WMBController controller;
    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        controller = WMBController.getInstance();
        controller.useMockURL(server.url("/").toString());
    }
    @Test
    public void testPOSTofAlert() throws Exception {
        server.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody("{\n" +
                "  \"id\": 1,\n" +
                "  \"user_id\": \"420\",\n" +
                "  \"issue_type\": \"construction\",\n" +
                "  \"description\": \"Bus driver was a ghost!\",\n" +
                "  \"upvotes\": 0,\n" +
                "  \"downvotes\": 0,\n" +
                "  \"neighborhood_id\": 1,\n" +
                "  \"created_at\": \"2016-11-10T17:29:53.626Z\"\n" +
                "}"));

        NeighborhoodAlert alert = controller.postAlertSynchronously(1,"construction", "Bus driver was a ghost!","420");

        assertEquals("alert description correct", "Bus driver was a ghost!", alert.getDescription());
        RecordedRequest request = server.takeRequest();
        assertEquals("request path", "/neighborhoods/1/alerts", request.getPath());
        assertEquals("request body", "issue_type=construction&description=Bus%20driver%20was%20a%20ghost%21&user_id=420", request.getBody().readUtf8());
    }

    @Test
    public void testPOSTofNeighborhoodAlertUpvote() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\n" +
                "  \"id\": 1,\n" +
                "  \"user_id\": \"420\",\n" +
                "  \"value\": \"up\"\n" +
                "}"));

        VoteConfirmation vc = controller.neighborhoodAlertUpvoteSynchronously(1, "420");
        assertEquals("id ", 1, vc.getId());
        assertEquals("user_id ", "420", vc.getUserID());
        assertEquals("value ", "up", vc.getValue());
        RecordedRequest request = server.takeRequest();
        assertEquals("request path", "/neighborhood_alerts/1/upvote", request.getPath());
    }


    @Test
    public void testGetNeighborhoods() throws Exception {
        server.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody("[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"Admiral\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 2,\n" +
                "    \"name\": \"Alki\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 3,\n" +
                "    \"name\": \"Ballard\"\n" +
                "  },\n" +
                "]"));

        List<Neighborhood> hoods = controller.getNeighborhoodsSynchonously();
        RecordedRequest request = server.takeRequest();
        assertEquals("request path: ", "/neighborhoods.json", request.getPath());
        assertEquals("neighborhood name: ", "Alki", hoods.get(1).getName());
    }

    @Test
    public void testPOSTNeighborhoodAlertComment() throws Exception {
        server.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody("{\n" +
                "  \"id\": 2,\n" +
                "  \"user_id\": \"666\",\n" +
                "  \"message\": \"nothing has ever been true\",\n" +
                "  \"upvotes\": 0,\n" +
                "  \"downvotes\": 0,\n" +
                "  \"created_at\": \"2016-11-10T19:07:11.769Z\"\n" +
                "}"));
        Comment comment = controller.postNeighborhoodAlertCommentSynchronously(2, "nothing has ever been true", "666");
        assertEquals(comment.getData(), "nothing has ever been true");
        RecordedRequest request = server.takeRequest();
        assertEquals("message=nothing%20has%20ever%20been%20true&user_id=666", request.getBody().readUtf8());
    }

    @Test
    public void testNeighborhoodAlertComments() throws Exception {
        server.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody("[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"user_id\": \"14\",\n" +
                "    \"message\": \"something about hitler\",\n" +
                "    \"upvotes\": 0,\n" +
                "    \"downvotes\": 0,\n" +
                "    \"created_at\": \"2016-11-10T17:51:32.984Z\"\n" +
                "  }\n" +
                "]"));
        List<Comment> comments = controller.getNeighborhoodAlertCommentsSynchronously(5);
        RecordedRequest request = server.takeRequest();
        assertEquals(comments.get(0).getData(), "something about hitler");
        assertEquals("/neighborhood_alerts/5/comments", request.getPath());
    }

    @After
    public void tearDown() throws Exception {
        controller.useProdURL();
        server.shutdown();
    }

}
