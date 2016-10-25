package modules;

import android.util.Pair;

import java.util.Set;

/**
 * Created by lidav on 10/23/2016.
 *
 * Class that stores data of an individual Bus Stop
 * Invariant: id = -1 if id has not been set, else id > 0
 *          routes != null
 *          coordinates == null only if it was not set yet
 */

public class BusStop {
    private int id;
    private Pair<Double, Double> coordinates;
    private Set<Route> routes;

    /**
     * Constructs a BusStop with id initialized to -1
     * @param coordinates Position of where the BusStop is
     * @param routes Set of Routes that stop at this BusStop
     * @throws IllegalArgumentException if coordinates or routes is null
     */
    public BusStop(Pair<Double, Double> coordinates, Set<Route> routes) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Sets the id of the BusStop
     * Does not set id if id was already set
     * @param id new id of the BusStop
     * @return true if id was set correctly, else return false
     * @throws IllegalArgumentException if id < 1
     */
    public boolean setId(int id) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets the id of the BusStop
     * @return if of the BusStop, -1 if id was not set
     */
    public int getId() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets a deep copy of the position of the BusStop
     * @return coordinates of the BusStop as a Pair<Double,Double>
     */
    public Pair<Double, Double> getCoordinates() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Geta deep copy of the Routes of Busses that stop at this BusStop
     * @return  routes that stop at the BusStop as a Set<Route>
     */
    public Set<Route> getRoutes() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
}
