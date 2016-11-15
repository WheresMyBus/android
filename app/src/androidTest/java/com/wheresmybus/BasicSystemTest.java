package com.wheresmybus;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Set;

import controllers.OBAController;
import controllers.WMBController;
import modules.Neighborhood;
import modules.NeighborhoodAlert;
import modules.Route;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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

    @Test
    public void sendRoutesRequestToAPI() throws Exception {
        OBAController controller = OBAController.getInstance();

        Set<Route> routeList = controller.getRoutesSynchonously();
        assertFalse(routeList.isEmpty());
        assertTrue(routeList.contains(new Route("556", "Issaquah University District Northgate", "40_100451")));
        //assertEquals("name comparison of first route", "Pacific to Algona to Auburn Station", routeList.get(0).getName());
    }

    /*  Async use example.
    public void exampleRequest() throws Exception {
        WMBController controller = WMBController.getInstance();
        controller.getNeighborhoods(new Callback<List<Neighborhood>>() {
            @Override
            public void onResponse(Response<List<Neighborhood>> response, Retrofit retrofit) {
                List<Neighborhood> data = response.body();
            }

            @Override
            public void onFailure(Throwable t) {
                // stuff to do when it doesn't work
            }
        });
    }
    */
    /*/ This is not a proper test, just stuff I have been trying to confirm that things work...
    @Test
    public void attemptPostToAPI() throws Exception {
        WMBController controller = WMBController.getInstance();

        NeighborhoodAlert responseAlert = controller.postAlertSynchronously(14, "construction", "Nothing matters anymore", 404);
        assertEquals("compare description", "Nothing matters anymore", responseAlert.getDescription());

        List<NeighborhoodAlert> alertsList = controller.getAlertsSynchronously(14);
        assertEquals("compare get to posted", alertsList.get(0).getDescription(), responseAlert.getDescription());

        assertEquals("Date: ", responseAlert.getDate(), alertsList.get(0).getDate());

    }


    @Test
    public void testDateInfo() throws Exception {
        WMBController controller = WMBController.getInstance();

        List<NeighborhoodAlert> alerts = controller.getAlertsSynchronously(3);
        assertEquals("2016-11-12T05:59:30.971Z", alerts.get(0).getDate().toString());

    }
    */

}
