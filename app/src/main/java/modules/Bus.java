package modules;

import android.util.Pair;

import junit.framework.Assert;

/**
 * Created by lidav on 10/23/2016.
 *
 * Class that stores data for an individual bus
 * Invariant: id = -1 only if id was not set yet, else id > 0
 *            route != null
 */

public class Bus {
    private int id;
    private Route route;
    private Pair<Double, Double> coordinates;

    /**
     * Constructs a Bus with id initialized to -1
     * @param route Route on which the bus runs on
     * @param coordinates Coordinates of where the bus is
     * @throws IllegalArgumentException if route == null
     */
    public Bus(Route route, Pair<Double, Double> coordinates) {
        if(route == null) {
            throw new IllegalArgumentException();
        }
        this.route = route;
        if(coordinates != null) {
            this.coordinates = new Pair<>(coordinates.first, coordinates.second);
        }
        checkRep();
    }

    /**
     * Sets the id of the bus
     * If the id was already set (id != -1), then does not set id
     * @param id int to set the id to
     * @return true if id was set correctly, else returns false
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
     * Gets the id of the bus
     * @return int id of the bus, -1 if id was not set
     */
    public int getId() {
        return id;
    }

    /**
     * Gets a deep copy of the Route the Bus is on
     * @return Route that the bus is travelling on
     */
    public Route getRoute() {
        return route; // Route is immutable
    }

    /**
     * Gets a deep copy of the position of the bus
     * @return coordinates of where the bus is
     */
    public Pair<Double, Double> getCoordinates() {
        return new Pair<>(coordinates.first, coordinates.second);
    }

    /**
     * Sets the coordinates of the bus
     * @param coordinates position fo the bus as a Pair<Double,Double>
     */
    public void setCoordinates(Pair<Double,Double> coordinates) {
        this.coordinates = coordinates;
        checkRep();
    }

    private void checkRep() {
        Assert.assertTrue(route != null);
        Assert.assertTrue(id == -1 || id > 0);
    }
}
