package com.wheresmybus;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import controllers.WMBController;
import modules.NeighborhoodAlert;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;

/**
 * Created by gunter on 11/11/16.
 */
@RunWith(AndroidJUnit4.class)
public class MockedInstrumentedTest {
    @Test
    public void testPOSTofAlert() throws Exception {
        MockWebServer server = new MockWebServer();
        server.start();
        WMBController controller = WMBController.getInstance();
        controller.useMockURL(server.url("/").toString());
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

        NeighborhoodAlert alert = controller.postAlertSynchronously(1,"construction", "Bus driver was a ghost!",420);

        assertEquals("alert description correct", "Bus driver was a ghost!", alert.getDescription());
        RecordedRequest request = server.takeRequest();
        assertEquals("request path", "/neighborhoods/1/alerts", request.getPath());
        assertEquals("request body", "issue_type=construction&description=Bus%20driver%20was%20a%20ghost%21&user_id=420", request.getBody().readUtf8());
        server.shutdown();
    }

}
