package modules;

import com.google.gson.annotations.SerializedName;

import android.util.Pair;

import junit.framework.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lidav on 10/23/2016.
 *
 * Class that stores data of an individual Bus Stop
 */

public class BusStop {
    @SerializedName("id")
    private String id;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lon")
    private double lon;
    @SerializedName("direction")
    private String direction;
    @SerializedName("name")
    private String name;
    @SerializedName("routes")
    private List<Route> routes;

    public BusStop(String id,
                   double lat,
                   double lon,
                   String direction,
                   String name,
                   List<Route> routes) {
        if(id == null) {
            throw new IllegalArgumentException("null id");
        } else if (direction == null) {
            throw new IllegalArgumentException("null direction");
        } else if (name == null) {
            throw new IllegalArgumentException("null name");
        } else if (routes == null) {
            throw new IllegalArgumentException("null routes");
        }
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.direction = direction;
        this.name = name;
        this.routes = routes;
    }

    public String getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getDirection() {
        return direction;
    }

    public String getName() {
        return name;
    }

    public List<Route> getRoutes() {
        return routes;
    }

}
