package modules;

import android.util.Pair;

import junit.framework.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by lidav on 10/23/2016.
 *
 * Class that stores data of an individual Bus Stop
 * Invariant: id = -1 if id has not been set, else id > 0
 *          routes != null
 *          coordinates != null
 */

public class BusStop {
    private int id;
    private Pair<Double, Double> coordinates;
    private Set<Route> routes;

    /**
     * Constructs a BusStop with id initialized to -1
     * @param coordinates Position of where the BusStop is
     * @param routes Set of Routes that stop at this BusStop
     * @throws IllegalArgumentException if coordinates or routes is null,
     *                                  or if routes contains null
     */
    public BusStop(Pair<Double, Double> coordinates, Set<Route> routes) {
        if(routes == null || coordinates == null) {
            throw new IllegalArgumentException();
        }
        if(routes.contains(null)) {
            throw new IllegalArgumentException();
        }
        this.coordinates = new Pair<>(coordinates.first, coordinates.second);
        this.routes = new HashSet<>(routes);
        checkRep();
    }

    /**
     * Sets the id of the BusStop
     * Does not set id if id was already set
     * @param id new id of the BusStop
     * @return true if id was set correctly, else return false
     * @throws IllegalArgumentException if id < 1
     */
    public boolean setId(int id) {
        if(id < 1) {
            throw new IllegalArgumentException();
        }
        if(this.id == -1) {
            this.id = id;
            checkRep();
            return true;
        }
        return false;
    }

    /**
     * Gets the id of the BusStop
     * @return if of the BusStop, -1 if id was not set
     */
    public int getId() {
        return id;
    }

    /**
     * Gets a deep copy of the position of the BusStop
     * @return coordinates of the BusStop as a Pair<Double,Double>
     */
    public Pair<Double, Double> getCoordinates() {
        return new Pair<>(coordinates.first, coordinates.second);
    }

    /**
     * Geta deep copy of the Routes of Busses that stop at this BusStop
     * @return  routes that stop at the BusStop as a Set<Route>
     */
    public Set<Route> getRoutes() {
        return new HashSet<>(routes);
    }

    private void checkRep() {
        Assert.assertTrue(routes != null);
        Assert.assertTrue(coordinates != null);
        Assert.assertTrue(id == -1 || id > 0);
        Assert.assertFalse(routes.contains(null));
    }
}
