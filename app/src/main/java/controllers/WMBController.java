package controllers;

import android.util.Pair;

import java.util.List;

import modules.Alert;
import modules.Neighborhood;

/**
 * Created by lidav on 10/23/2016.
 *
 * Controller that receives information from Where'sMyBus backend server and database
 */

public class WMBController {
    /**
     * Constructs a WMBController and connects to server/database
     */
    public  WMBController() {

    }

    public List<Alert> getAlerts() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
    public List<Alert> getAlerts(int routeNumber) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
    public List<Alert> getAlerts(Neighborhood neighborhood) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
    public List<Alert> getAlerts(Pair<Double,Double> center, double radius) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
    public boolean postAlert(Alert alert) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
    public boolean upvote(Alert alert) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
    public boolean downvote(Alert alert) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
    public List<Neighborhood> getNeighborhoods() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
}
