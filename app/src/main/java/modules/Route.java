package modules;

import junit.framework.Assert;

import java.util.Set;

/**
 * Created by lidav on 10/23/2016.
 *
 * Immutable class that stores data for a bus route
 * Invariant: number > 0, name != null
 */

public class Route {
    private int number;
    private String name;
    //private Set<BusStop> busStops;
    //private Set<Bus> busses;

    /**
     * Constructs a Route
     * If busStops or busses is null, creates empty sets for them
     * @param number number that Route is associated with
     * @param name destination of the Route
     * @throws IllegalArgumentException if number < 1
     * @throws IllegalArgumentException if name == null
     */
    public Route(int number, String name) {
        if(number < 1) {
            throw new IllegalArgumentException("number < 1");
        }
        if(name == null) {
            throw new IllegalArgumentException("name == null");
        }
        this.number = number;
        this.name = name;
        checkRep();
    }

    /**
     * Returns the full name of the Route, number and name
     * @return full name of route
     */
    public String toString() {
        return number + " " + name;
    }

    /**
     * Gets the number of the route
     * @return number of the route as an int
     */
    public int getNumber() {
        return number;
    }

    /**
     * Gets the name of the route
     * @return name of the route as a String
     */
    public String getName() {
        return name;
    }
    private void checkRep() {
        Assert.assertTrue(number > 0);
        Assert.assertTrue(name != null);
    }
/*
    /**
     * Gets a deep copy of the BusStops in this route
     * @return BusStops in the route as a set

    public Set<BusStop> getBusStops() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Adds a bus stop to the Route
     * @param stop BusStop to add
     * @throws IllegalArgumentException if stop is null

    public void addBusStop(BusStop stop) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets a deep copy of the busses in this route
     * @return busses in the route as a set

    public Set<Bus> getBusses() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Adds a bus to a Route
     * @param bus Bus to add to the route
     * @throws IllegalArgumentException if bus is null

    public void addBus(Bus bus) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public void removeBus() */
}
