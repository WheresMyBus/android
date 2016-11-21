package UITests;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.wheresmybus.AlertForumActivity;
import com.wheresmybus.R;
import com.wheresmybus.SubmitAlertActivity;

import java.io.IOException;

import controllers.WMBController;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
/**
 * Created by lidav on 11/18/2016.
 * Tests for AlertForumActivity
 */

public class TestAlertForumActivity {
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
    public ActivityTestRule<AlertForumActivity> rule =
            new ActivityTestRule<>(AlertForumActivity.class);

    @Test
    public void testAlertDisplay() throws IOException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("[" +
                        "{" +
                        "\"id\":1," +
                        "\"user_id\":\"[User ID]\"," +
                        "\"issues\":\"Maintenance Issue\"," +
                        "\"description\":\"Front tire blew out\"," +
                        "\"upvotes\":0," +
                        "\"downvotes\":0," +
                        "\"route_id\":\"1_100224\"," +
                        "\"created_at\":\"2016-11-16T17:39:56.292Z\"" +
                        "}" +
                        "]"));
        server.start();
        controller.useMockURL(server.url("/").toString());
        Intent startIntent = new Intent();
        startIntent.putExtra("TAB_INDEX", 1);
        rule.launchActivity(startIntent);
    }

    @After
    public void tearDown() throws Exception {
        controller.useProdURL();
        server.shutdown();
    }
}
