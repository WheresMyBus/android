package UITests;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.wheresmybus.R;
import com.wheresmybus.SubmitAlertActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by whitldy on 11/15/2016.
 */
@RunWith(AndroidJUnit4.class)
public class TestSubmitAlert {
    @Rule
    public ActivityTestRule<SubmitAlertActivity> submitAlertActivityRule =
            new ActivityTestRule<SubmitAlertActivity>(SubmitAlertActivity.class);

    /**
     * Tests that route alert fragment is invisible at first, then is displayed after the
     * radio bus route button is clicked.
     */
    @Test
    public void changeFragment() {
        onView(withId(R.id.bus_route_alert_fragment))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        onView(withId(R.id.radio_bus_route))
                .perform(click());
        onView(withId(R.id.bus_route_alert_fragment))
                .check(matches(isDisplayed()));
    }
}
