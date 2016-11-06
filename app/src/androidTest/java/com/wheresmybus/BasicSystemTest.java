package com.wheresmybus;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import controllers.WMBController;
import modules.Neighborhood;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BasicSystemTest {


    @Test
    public void sendRequestToAPI() throws Exception {
        WMBController controller = WMBController.getInstance();

        List<Neighborhood> hoods = controller.getNeighborhoodsSynchonously();
        Neighborhood maybeLaurelhurst = hoods.get(27);
        assertEquals("Name comparison of index 27 neighborhood entry", "Laurelhurst", maybeLaurelhurst.getName());
        assertEquals("ID comparison of index 27 neighborhood entry", 28, maybeLaurelhurst.getID());

        Neighborhood maybeSouthPark = hoods.get(47);
        assertEquals("Name comparison of index 48 neighborhood entry", "South Park", maybeSouthPark.getName());
        assertEquals("ID comparison of index 48 neighborhood entry", 48, maybeSouthPark.getID());

    }
}
