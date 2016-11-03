package modules;

import android.util.Pair;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by lidav on 10/23/2016.
 *
 * Immutable class that describes a neighborhood
 * Invariant: perimeter, name != null
 *          no element of perimeter is null
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
     * @throws IllegalArgumentException if any element of perimeter is null
     */
    public Neighborhood(List<Pair<Double,Double>> perimeter, String name) {
        if(perimeter == null || perimeter.contains(null) || name == null) {
            throw new IllegalArgumentException();
        }
        for(Pair<Double,Double> next : perimeter) {
            this.perimeter.add(new Pair<>(next.first, next.second));
        }
        this.name = name;
        checkRep();
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

    private void checkRep() {
        Assert.assertFalse(name == null);
        Assert.assertFalse(perimeter == null);
        Assert.assertFalse(perimeter.contains(null));
    }

    /*private Set<BusStop> getBusStops() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }*/
}
