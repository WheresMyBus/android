package UITests;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.wheresmybus.CatalogActivity;
import com.wheresmybus.R;
import com.wheresmybus.SubmitAlertActivity;

import controllers.OBAController;
import controllers.WMBController;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.StringStartsWith.startsWith;

/**
 * Created by whitldy on 11/15/2016.
 */
@RunWith(AndroidJUnit4.class)
public class TestSubmitAlert {
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
                }  else if (request.getPath().equals("/neighborhoods.json")){
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
                } else if (request.getPath().equals("neighborhoods/2/alerts")) {
                    return new MockResponse()
                            .setResponseCode(200)
                            .setBody("{\n" +
                                    "  \"id\": 1,\n" +
                                    "  \"user_id\": \"50d1ce8e-a213-40a0-8228-587ea7fd604c\",\n" +
                                    "  \"issues\": \"construction\",\n" +
                                    "  \"description\": \"Alert description goes here...\",\n" +
                                    "  \"upvotes\": 0,\n" +
                                    "  \"downvotes\": 0,\n" +
                                    "  \"neighborhood_id\": 1,\n" +
                                    "  \"created_at\": \"2016-11-10T17:29:53.626Z\",\n" +
                                    "  \"routes_affected\": [\n" +
                                    "    {\n" +
                                    "      \"id\": \"1_100224\",\n" +
                                    "      \"number\": \"44\",\n" +
                                    "      \"name\": \"Ballard - Montlake\"\n" +
                                    "    }\n" +
                                    "  ]\n" +
                                    "}");
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
    public ActivityTestRule<SubmitAlertActivity> rule =
            new ActivityTestRule<SubmitAlertActivity>(SubmitAlertActivity.class);

    /**
     * checks that the route fragment becomes visible after selecting bus route.
     */
    @Test
    public void testChangeToRouteFragment() {
        onView(withId(R.id.bus_route_alert_fragment))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.radio_bus_route))
                .perform(click());
        onView(withId(R.id.bus_route_alert_fragment))
                .check(matches(isDisplayed()));
    }

    /**
     * Checks that the neighborhood fragment becomes visible after selecting
     * neighborhood.
     */
    @Test
    public void testChangeToNeighborhoodFragment() {
        onView(withId(R.id.neighborhood_alert_fragment))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.radio_neighborhood))
                .perform(click());
        onView(withId(R.id.neighborhood_alert_fragment))
                .check(matches(isDisplayed()));
    }

    /**
     * checks that selecting route then neighborhood makes route invisible and neighborhood visible.
     */
    @Test
    public void testAlternateAlertType() {
        onView(withId(R.id.bus_route_alert_fragment))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.neighborhood_alert_fragment))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.radio_bus_route))
                .perform(click());
        onView(withId(R.id.bus_route_alert_fragment))
                .check(matches(isDisplayed()));
        onView(withId(R.id.neighborhood_alert_fragment))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));

        onView(withId(R.id.radio_neighborhood))
                .perform(click());
        onView(withId(R.id.neighborhood_alert_fragment))
                .check(matches(isDisplayed()));
        onView(withId(R.id.bus_route_alert_fragment))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));

    }

    /**
     * tests that clicking on spinner option Alki causes the spinner to display Alki.
     */
    @Test
    public void testSpinnerTextSelection() {
        onView(withId(R.id.radio_neighborhood))
                .perform(click());
        onView(withId(R.id.neighborhood_spinner))
                .perform(click());
        onData(hasToString(startsWith("Alki")))
        .perform(click());
        onView(withId(R.id.neighborhood_spinner))
                .check(matches(withSpinnerText("Alki")));
    }

    @Test
    public void testCompleteNeighborhoodAlert() throws InterruptedException {
        onView(withId(R.id.radio_neighborhood))
                .perform(click());
        onView(withId(R.id.neighborhood_spinner))
                .perform(click());
        onData(hasToString(startsWith("Alki")))
                .perform(click());
        onView(withId(R.id.checkBox5))
                .perform(click());
        onView(withId(R.id.neighborhood_alert_description))
                .perform(typeText("whatever you want"));
        onView(withId(R.id.button_submit))
                .perform(click());
        RecordedRequest request = server.takeRequest();
        request.getBody().readUtf8().equals("issues=construction&description=whateveryouwant&user_id=1234&affected_routes[]=[]");
    }


}
