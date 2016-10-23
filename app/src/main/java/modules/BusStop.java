package modules;

import android.util.Pair;

import java.util.Set;

/**
 * Created by lidav on 10/23/2016.
 *
 * Class that stores data of an individual Bus Stop
 */

public class BusStop {
    private int id;
    private Pair<Double, Double> coordinates;
    private Set<Route> routes;

    public BusStop(int id, Pair<Double, Double> coordinates, Set<Route> routes) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public boolean setId(int id) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public int getId() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public Pair<Double, Double> getCoordinates() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public Set<Route> getRoutes() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
}
