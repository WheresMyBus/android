package modules;

import com.google.gson.annotations.SerializedName;

import junit.framework.Assert;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by lidav on 10/23/2016.
 *
 * Immutable class that stores data for a bus route
 * Invariant: number > 0, name != null
 */

public class Route implements Serializable, Comparable<Route> {
    @SerializedName("number")
    private String number;
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private String id;

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
        return "[" + number + "] " + name;
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

    /**
     * Returns true iff name, number, and id all match, else return false
     * @param other Route to compare to
     * @return true if name, number, and id all match for both routes, else return false
     */
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

    /**
     * Returns < 0 if the other route has a lower number
     * returns 0 if the other route has the same number
     * else returns > 0
     * @param o Route to compare to
     */
    @Override
    public int compareTo(Route o) {
        int number1 = 0;
        int number2 = 0;
        boolean isNumber1 = true;
        boolean isNumber2 = true;

        try {
            number1 = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            isNumber1 = false;
        }

        try {
            number2 = Integer.parseInt(o.number);
        } catch (NumberFormatException e) {
            isNumber2 = false;
        }

        if (isNumber1 && isNumber2) {
            return number1 - number2;
        } else {
            return number.compareTo(o.number);
        }
    }
}
