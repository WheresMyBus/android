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
 * Invariant: name != null, id == -1 if not initialized or id > 0
 */

public class Neighborhood implements Serializable, Comparable<Neighborhood> {
    private List<Pair<Double,Double>> perimeter;

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    /**
     * Contructs a Neighborhood
     * @param id unique int ID of the neighborhood.
     * @param name name of the neighborhood
     * @throws IllegalArgumentException if name is null or id < 0.
     */
    public Neighborhood(int id, String name) {
        if(name == null) {
            throw new IllegalArgumentException("null name");
        }
        if(id < 1) {
            throw new IllegalArgumentException("bad id");
        }
        this.name = name;
        this.id = id;
        checkRep();
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
    }

    /**
     * Allow Neighborhoods to be compared by their names
     * @param o Neighborhood to compare to
     * @return < 0 if other neighborhood's name comes alphabetically before this
     *              neighborhood's, 0 if both names are identical, else > 0
     */
    @Override
    public int compareTo(Neighborhood o) {
        return name.compareTo(o.name);
    }

    /**
     * Returns true if the given object is a neighborhood with the same ID as this one.
     *
     * @param obj the object to be checked for equality
     * @return true if the given object is a neighborhood with the same ID as this one or false
     *              otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Neighborhood) {
            return this.id == ((Neighborhood) obj).id;
        } else {
            return false;
        }
    }

    /**
     * Returns the hash code of this neighborhood.
     *
     * @return the hash code of this neighborhood
     */
    @Override
    public int hashCode() {
        return (this.getName() + this.getID()).hashCode();
    }
}
