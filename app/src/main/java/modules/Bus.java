package modules;

import android.util.Pair;

import com.google.gson.annotations.SerializedName;

import junit.framework.Assert;

/**
 * Created by lidav on 10/23/2016.
 *
 * Class that stores data for an individual bus
 * Invariant: id = -1 only if id was not set yet, else id > 0
 *            route != null
 */

public class Bus {
    @SerializedName("lat")
    private double lat;
    @SerializedName("lon")
    private double lon;


    /**
     * Creates a Bus with a latitude and longitude
     *
     * @param lat
     * @param lon
     */
    public Bus(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * @return the latitude of the bus
     */
    public double getLat() {
        return this.lat;
    }

    /**
     * @return the longitude of the bus
     */
    public double getLon() {
        return this.lon;
    }
}