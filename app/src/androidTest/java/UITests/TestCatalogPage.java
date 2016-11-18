package UITests;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.wheresmybus.CatalogActivity;
import com.wheresmybus.R;
import com.wheresmybus.SubmitAlertActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import controllers.OBAController;
import controllers.WMBController;
import modules.UserDataManager;
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

public class TestCatalogPage {
    private WMBController controller;
    private OBAController controller2;
    @Before
    public void setUp() throws Exception {
        controller = WMBController.getInstance();
        controller2 = OBAController.getInstance();
    }

    @Rule
    public ActivityTestRule<CatalogActivity> rule =
            new ActivityTestRule<CatalogActivity>(CatalogActivity.class, true, false);

    @After
    public void tearDown() throws Exception {
        //controller.useProdURL();
    }

    @Test
    public void testNeighborhoodCatalogPopulated() throws Exception {
        MockWebServer server = new MockWebServer();
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
        server.start();
        controller.useMockURL(server.url("/").toString());
        controller2.useMockURL(server.url("/").toString());
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

//        onData(anything()).inAdapterView(withId(R.id.neighborhood_list))
//                .atPosition(2)
//                .onChildView(withId(R.id.star))
//                .perform(click());

        //TODO: figure out a way to test the favorites button changing colors.
//        onData(anything()).inAdapterView(withId(R.id.neighborhood_list))
//                .atPosition(2)
//                .onChildView(withId(R.id.star))
//                .check(matches(withTint(R.color.yellow)));
        server.shutdown();
    }

    @Test
    public void testNeighborhoodSelection() throws Exception {
        MockWebServer server = new MockWebServer();
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
        server.start();
        controller.useMockURL(server.url("/").toString());
        controller2.useMockURL(server.url("/").toString());

        Intent startIntent = new Intent();
        startIntent.putExtra("TAB_INDEX", 1);
        rule.launchActivity(startIntent);

        Intents.init();

        onData(anything()).inAdapterView(withId(R.id.neighborhood_list))
                .atPosition(2)
                .onChildView(withId(R.id.name))
                .perform(click());

        intended(hasComponent("com.wheresmybus.AlertForumActivity"));
        intended(hasExtra("ALERT_TYPE", "Neighborhood"));
        intended(hasExtra("NEIGHBORHOOD_ID", 3));
        server.shutdown();
    }


    @Test
    public void testRouteCatalogPopulated() throws Exception {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse()
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
                        "]\n"));
        server.start();
        controller.useMockURL(server.url("/").toString());
        controller2.useMockURL(server.url("/").toString());

        Intent startIntent = new Intent();
        startIntent.putExtra("TAB_INDEX", 0);
        rule.launchActivity(startIntent);
        RecordedRequest request = server.takeRequest();
        assertEquals("/routes.json", request.getPath());
        onData(anything()).inAdapterView(withId(R.id.route_list))
                .atPosition(0)
                .onChildView(withId(R.id.number))
                .check(matches(withText("37"))); // NOTE: They are sorted by number!

                //.check(matches(withText("917")));
//        onData(anything()).inAdapterView(withId(R.id.route_list))
//                .atPosition(0)
//                .onChildView(withId(R.id.name))
//                .check(matches(withText("Pacific to Algona to Auburn Station")));
//
//        onData(anything()).inAdapterView(withId(R.id.route_list))
//                .atPosition(1)
//                .onChildView(withId(R.id.number))
//                .check(matches(withText("A Line")));
//        onData(anything()).inAdapterView(withId(R.id.route_list))
//                .atPosition(1)
//                .onChildView(withId(R.id.name))
//                .check(matches(withText("Federal Way TC/Tukwila International Blvd Link Sta")));
//
//        onData(anything()).inAdapterView(withId(R.id.route_list))
//                .atPosition(2)
//                .onChildView(withId(R.id.number))
//                .check(matches(withText("37")));
//        onData(anything()).inAdapterView(withId(R.id.route_list))
//                .atPosition(2)
//                .onChildView(withId(R.id.name))
//                .check(matches(withText("Alaska Junction to Alki to Downtown Seattle")));

        server.shutdown();
    }



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
