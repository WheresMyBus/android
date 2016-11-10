package modules;

import com.google.gson.annotations.SerializedName;

import junit.framework.Assert;

import java.util.Set;

/**
 * Created by lidav on 10/23/2016.
 *
 * Immutable class that stores data for a bus route
 * Invariant: number > 0, name != null
 */

public class Route {
    @SerializedName("number")
    private String number;
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private String id;

    //private Set<BusStop> busStops;
    //private Set<Bus> busses;

    /**
     * Constructs a Route
     * If busStops or busses is null, creates empty sets for them
     * @param number number that Route is associated with
     * @param name destination of the Route
     * @param id the route's OBA id
     * @throws IllegalArgumentException if number == null
     * @throws IllegalArgumentException if name == null
     * @throws IllegalArgumentException if id == null
     */
    public Route(String number, String name, String id) {
        if(number == null) {
            throw new IllegalArgumentException("number == null");
        }
        if(name == null) {
            throw new IllegalArgumentException("name == null");
        }
        if (id == null) {
            throw new IllegalArgumentException("id == null");
        }
        this.number = number;
        this.name = name;
        this.id = id;
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
    public String getNumber() {
        return number;
    }

    /**
     * Gets the name of the route
     * @return name of the route as a String
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the OBA id of the route
     * @return id of the route as a String
     */
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object other) {
        boolean res = true;
        if (other != null && other instanceof Route) {
            return this.getName().equals(((Route) other).getName())
                    && this.getNumber().equals(((Route) other).getNumber())
                    && this.getId().equals(((Route) other).getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (this.getName() + this.getNumber() + this.getId()).hashCode();
    }

    private void checkRep() {
        Assert.assertTrue(number != null);
        Assert.assertTrue(name != null);
        Assert.assertTrue(id != null);
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
