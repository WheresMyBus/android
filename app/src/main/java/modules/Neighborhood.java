package modules;

import android.util.Pair;

import com.google.gson.annotations.SerializedName;

import junit.framework.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Created by lidav on 10/23/2016.
 *
 * Immutable class that describes a neighborhood
 * Invariant: perimeter, name != null
 *          no element of perimeter is null
 */

public class Neighborhood implements Serializable, Comparable<Neighborhood> {
    private List<Pair<Double,Double>> perimeter;

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    //private Pair<Double, Double> center;
    //private double radius;
    // private Set<BusStop> busStops;

    /**
     * Contructs a Neighborhood
     * @param id unique int ID of the neighborhood.
     * //@param perimeter List<Pair<Double,Double>> of Points in the perimeter of the neighborhood
     * @param name name of the neighborhood
     * @throws IllegalArgumentException if perimeter or name is null or id < 0.
     * @throws IllegalArgumentException if any element of perimeter is null
     */
    public Neighborhood(int id, String name) {
        /* TODO: adjust once perimeter data is available. Then uncomment with checkRep().
        if(perimeter == null || perimeter.contains(null) || name == null) {
            throw new IllegalArgumentException();
        }
        for(Pair<Double,Double> next : perimeter) {
            this.perimeter.add(new Pair<>(next.first, next.second));
        }
        */
        if(id < 1 || name == null) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.id = id;
        //checkRep();
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
        ArrayList<Pair<Double,Double>> newList = new ArrayList<>();
        for(Pair<Double,Double> next : perimeter) {
            newList.add(new Pair<>(next.first, next.second));
        }
        return newList;
    }

    /**
     * Gets the name of the neighborhood
     * @return name of the neighborhood
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the name of the neighborhood
     * @return name of the neighborhood
     */
    public int getID() {
        return id;
    }


    /**
     * Gets the string representation of the neighborhood
     * @return string representation of the neighborhood
     */
    @Override
    public String toString() {
        return name;
    }

    private void checkRep() {
        Assert.assertFalse(name == null);
        //Assert.assertFalse(perimeter == null);
        //Assert.assertFalse(perimeter.contains(null));
    }

    @Override
    public int compareTo(Neighborhood o) {
        return name.compareTo(o.name);
    }

    /*private Set<BusStop> getBusStops() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }*/
}
