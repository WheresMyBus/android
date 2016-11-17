package UITests;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import com.wheresmybus.CatalogActivity;
import com.wheresmybus.SubmitAlertActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import controllers.WMBController;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

/**
 * Created by myself on 11/17/16.
 */

public class TestCatalogPage {
    private MockWebServer server;
    private WMBController controller;
    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        controller = WMBController.getInstance();
        controller.useMockURL(server.url("/").toString());
    }

    @Rule
    public ActivityTestRule<CatalogActivity> rule =
            new ActivityTestRule<CatalogActivity>(CatalogActivity.class, true, false);

    @After
    public void tearDown() throws Exception {
        controller.useProdURL();
        server.shutdown();
    }

    @Test
    public void testCatalogPopulated() {
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
                "  }\n" +
                "]"));

        Intent startIntent = new Intent();
        startIntent.putExtra("TAB_INDEX", 1);
        rule.launchActivity(startIntent);
        // TODO: figure out how to check list.
    }

}
