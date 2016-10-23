package modules;

import android.util.Pair;

import java.util.Set;

/**
 * Created by lidav on 10/23/2016.
 *
 * Class that describes a neighborhood as a circle
 */

public class Neighborhood {
    private int id;
    private Pair<Double, Double> center;
    private double radius;
    private Set<BusStop> busStops;

    public Neighborhood(int id, Pair<Double, Double> center, double radius) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public boolean setId(int id) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public int getId() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public Pair<Double, Double> getCenter() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public double getRadius() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    private Set<BusStop> getBusStops() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
}
