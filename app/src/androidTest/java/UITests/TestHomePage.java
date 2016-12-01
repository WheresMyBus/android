package UITests;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import com.wheresmybus.CatalogActivity;
import com.wheresmybus.MainActivity;
import com.wheresmybus.R;
import com.wheresmybus.SubmitAlertActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import controllers.WMBController;
import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.release;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasType;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Dylan on 11/17/2016.
 */

public class TestHomePage {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void tearDown() {
        release();
    }

    @Test
    public void testBusRoutesButton() {
        onView(withId(R.id.button4)).perform(click());
        intended(hasComponent("com.wheresmybus.RouteMainActivity"));
    }

    @Test
    public void testNeighborhoodButton() {
        onView(withId(R.id.button3)).perform(click());
        intended(hasComponent("com.wheresmybus.CatalogActivity"));
        intended(hasExtra("TAB_INDEX", 1));
    }

    @Test
    public void testSubmitAlertButton() {
        onView(withId(R.id.button)).perform(click());
        intended(hasComponent("com.wheresmybus.SubmitAlertActivity"));
    }
}
