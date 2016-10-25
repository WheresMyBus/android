package modules;

import android.util.Pair;

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
     * @throws IllegalArgumentException if Route == null
     */
    public Bus(Route route, Pair<Double, Double> coordinates) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Sets the id of the bus
     * If the id was already set (id != -1), then does not set id
     * @param id int to set the id to
     * @return true if id was set correctly, else returns false
     */
    public boolean setId(int id) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets the id of the bus
     * @return int id of the bus, -1 if id was not set
     * @throws IllegalArgumentException if param id < 1
     */
    public int getId() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets a deep copy of the Route the Bus is on
     * @return Route that the bus is travelling on
     */
    public Route getRoute() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets a deep copy of the position of the bus
     * @return Pair<Double,Double> coordinates of where the bus is
     */
    public Pair<Double, Double> getCoordinates() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Sets the coordinates of the bus
     * @param coordinates position fo the bus as a Pair<Double,Double>
     * @throws IllegalArgumentException if coordinates == null
     */
    public void setCoordinates(Pair<Double,Double> coordinates) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
}
