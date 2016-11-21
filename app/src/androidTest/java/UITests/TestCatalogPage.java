package UITests;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.internal.runner.junit3.JUnit38ClassRunner;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.wheresmybus.CatalogActivity;
import com.wheresmybus.R;
import com.wheresmybus.SubmitAlertActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import controllers.OBAController;
import controllers.WMBController;
import modules.UserDataManager;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertEquals;

/**
 * Created by myself on 11/17/16.
 */
@RunWith(AndroidJUnit4.class)
public class TestCatalogPage {
    private static WMBController controller;
    private static OBAController controller2;
    private static MockWebServer server;
    @BeforeClass
    public static void setUpClass() throws Exception {
        controller = WMBController.getInstance();
        controller2 = OBAController.getInstance();
        server = new MockWebServer();
        server.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                if (request.getPath().equals("/routes.json")){
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("[\n" +
                                    "  {\n" +
                                    "    \"id\": \"1_100300\",\n" +
                                    "    \"number\": \"917\",\n" +
                                    "    \"name\": \"Pacific to Algona to Auburn Station\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"id\": \"1_100512\",\n" +
                                    "    \"number\": \"A Line\",\n" +
                                    "    \"name\": \"Federal Way TC/Tukwila International Blvd Link Sta\"\n" +
                                    "  },\n" +
                                    "  {\n" +
                                    "    \"id\": \"1_100212\",\n" +
                                    "    \"number\": \"37\",\n" +
                                    "    \"name\": \"Alaska Junction to Alki to Downtown Seattle\"\n" +
                                    "  }\n" +
                                    "]\n");
                } else if (request.getPath().equals("/neighborhoods.json")){
                    return new MockResponse()
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
                                    "]");
                } else if (request.getPath().equals("/neighborhoods/3/alerts"))
                {
                    return new MockResponse().setResponseCode(200)
                            .setBody("[\n" +
                                    "  {\n" +
                                    "    \"id\": 1,\n" +
                                    "    \"user_id\": \"50d1ce8e-a213-40a0-8228-587ea7fd604c\",\n" +
                                    "    \"issues\": \"construction\",\n" +
                                    "    \"description\": \"Alert description goes here...\",\n" +
                                    "    \"upvotes\": 0,\n" +
                                    "    \"downvotes\": 0,\n" +
                                    "    \"neighborhood_id\": 1,\n" +
                                    "    \"created_at\": \"2016-11-10T17:29:53.626Z\"\n" +
                                    "  }\n" +
                                    "]");
                } else if (request.getPath().equals("/routes/1_100512/alerts")) {
                    return new MockResponse().setResponseCode(200)
                            .setBody("[\n" +
                                    "  {\n" +
                                    "    \"id\": 1,\n" +
                                    "    \"user_id\": \"1d5a07f3-e980-49b7-bf6c-005a02fe3e13\",\n" +
                                    "    \"issues\": \"construction\",\n" +
                                    "    \"description\": \"Alert description goes here...\",\n" +
                                    "    \"upvotes\": 0,\n" +
                                    "    \"downvotes\": 0,\n" +
                                    "    \"route_id\":\"1_100224\",\n" +
                                    "    \"created_at\": \"2016-11-11T00:28:53.935Z\"\n" +
                                    "  }\n" +
                                    "]");
                } else {
                    return null;
                }
            }
        });
        server.start();
        controller.useMockURL(server.url("/").toString());
        controller2.useMockURL(server.url("/").toString());
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        controller.useProdURL();
        controller2.useProdURL();
        server.shutdown();
    }

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Rule
    public ActivityTestRule<CatalogActivity> rule =
            new ActivityTestRule<CatalogActivity>(CatalogActivity.class, true, false);

    @Test
    public void testRouteCatalogPopulated() throws Exception {
        Intent startIntent = new Intent();
        startIntent.putExtra("TAB_INDEX", 0);
        rule.launchActivity(startIntent);
        onData(anything()).inAdapterView(withId(R.id.route_list))
                .atPosition(0)
                .onChildView(withId(R.id.number))
                .check(matches(withText("37"))); // NOTE: They are sorted by number!

        onData(anything()).inAdapterView(withId(R.id.route_list))
                .atPosition(1)
                .onChildView(withId(R.id.name))
                .check(matches(withText("Pacific to Algona to Auburn Station")));
        onData(anything()).inAdapterView(withId(R.id.route_list))
                .atPosition(1)
                .onChildView(withId(R.id.number))
                .check(matches(withText("917")));
        onData(anything()).inAdapterView(withId(R.id.route_list))
                .atPosition(2)
                .onChildView(withId(R.id.number))
                .check(matches(withText("A Line")));
        onData(anything()).inAdapterView(withId(R.id.route_list))
                .atPosition(2)
                .onChildView(withId(R.id.name))
                .check(matches(withText("Federal Way TC/Tukwila International Blvd Link Sta")));

        onData(anything()).inAdapterView(withId(R.id.route_list))
                .atPosition(0)
                .onChildView(withId(R.id.name))
                .check(matches(withText("Alaska Junction to Alki to Downtown Seattle")));

}

    @Test
    public void testNeighborhoodCatalogPopulated() throws Exception {
        Intent startIntent = new Intent();
        startIntent.putExtra("TAB_INDEX", 1);
        rule.launchActivity(startIntent);

        onData(anything()).inAdapterView(withId(R.id.neighborhood_list))
                .atPosition(0)
                .onChildView(withId(R.id.name))
                .check(matches(withText("Admiral")));
        onData(anything()).inAdapterView(withId(R.id.neighborhood_list))
                .atPosition(1)
                .onChildView(withId(R.id.name))
                .check(matches(withText("Alki")));
        onData(anything()).inAdapterView(withId(R.id.neighborhood_list))
                .atPosition(2)
                .onChildView(withId(R.id.name))
                .check(matches(withText("Ballard")));

        //TODO: figure out a way to test the favorites button changing colors.
//        onData(anything()).inAdapterView(withId(R.id.neighborhood_list))
//                .atPosition(2)
//                .onChildView(withId(R.id.star))
//                .check(matches(withTint(R.color.yellow)));
    }

    @Test
    public void testNeighborhoodSelection() throws Exception {
        Intent startIntent = new Intent();
        startIntent.putExtra("TAB_INDEX", 1);
        rule.launchActivity(startIntent);

        onData(anything()).inAdapterView(withId(R.id.neighborhood_list))
                .atPosition(2)
                .onChildView(withId(R.id.name))
                .perform(click());

        intended(hasComponent("com.wheresmybus.AlertForumActivity"));
        intended(hasExtra("ALERT_TYPE", "Neighborhood"));
        intended(hasExtra("NEIGHBORHOOD_ID", 3));
    }

    @Test
    public void testRouteSelection() throws Exception {
        Intent startIntent = new Intent();
        startIntent.putExtra("TAB_INDEX", 0);
        rule.launchActivity(startIntent);

        onData(anything()).inAdapterView(withId(R.id.route_list))
                .atPosition(2)
                .onChildView(withId(R.id.name))
                .perform(click());

        intended(hasComponent("com.wheresmybus.AlertForumActivity"));
        intended(hasExtra("ALERT_TYPE", "Route"));
        intended(hasExtra("ROUTE_ID", "1_100512"));
    }




//// This does not work. But a matcher will probably be necessary for testing color changes.
//    public static Matcher<View> withTint(final int color) {
//        return new BoundedMatcher<View, View>(ImageButton.class) {
//
//            @Override
//            protected boolean matchesSafely(View item) {
//                // dismissed some API > 15 related warning for this next line.
//                Log.d("Tint name: ", ((ImageButton) item).getColorFilter().toString());
//                return color == ((ImageButton) item).getImageTintList().getDefaultColor();
//            }
//
//            @Override
//            public void describeTo(Description description) {
//
//            }
//        };
//    }
}
