package modules;

import android.util.Pair;

/**
 * Created by lidav on 10/23/2016.
 *
 * Class that stores data for an individual bus
 */

public class Bus {
    private int id;
    private Route route;
    private Pair<Double, Double> coordinates;

    public Bus(Route route, Pair<Double, Double> coordinates) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public boolean setId(int id) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public int getId() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public Route getRoute() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public  Pair<Double, Double> getCoordinates() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
}
