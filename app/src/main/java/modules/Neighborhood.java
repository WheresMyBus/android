package modules;

import android.util.Pair;

import java.util.List;
import java.util.Set;

/**
 * Created by lidav on 10/23/2016.
 *
 * Immutable class that describes a neighborhood
 * Invariant: perimeter, name != null
 */

public class Neighborhood {
    private List<Pair<Double,Double>> perimeter;
    //private int id;
    private String name;
    //private Pair<Double, Double> center;
    //private double radius;
    // private Set<BusStop> busStops;

    /**
     * Contructs a Neighborhood
     * @param perimeter List<Pair<Double,Double>> of Points in the perimeter of the neighborhood
     * @param name name of the neighborhood
     * @throws IllegalArgumentException if perimeter or name is null
     */
    public Neighborhood(List<Pair<Double,Double>> perimeter, String name) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /*public boolean setId(int id) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public int getId() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    } */

    /**
     * Gets a deep copy of the perimeter list
     * @return perimeter of the neighborhood as a List of Pairs
     */
    public List<Pair<Double, Double>> getPerimeter() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets the name of the neighborhood
     * @return name of the neighborhood
     */
    public String getName() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /*private Set<BusStop> getBusStops() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }*/
}
