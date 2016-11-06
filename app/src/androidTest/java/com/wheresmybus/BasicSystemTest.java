package com.wheresmybus;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import controllers.WMBController;
import modules.Neighborhood;
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
}
