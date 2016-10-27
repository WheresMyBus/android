package com.wheresmybus;

import java.util.*;
import modules.Alert;
import modules.Neighborhood;

/**
 * Created by lidav on 10/23/2016.
 */

public class AlertDisplay {

    /**
     * Gets the alerts for a particular route
     * @param routeNumber the number of the route (as an int) to get alerts for
     * @return the list of alerts associated with the given route
     */
    public List<Alert> getAlertsForRoute(int routeNumber) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Gets the alerts for a particular neighborhood
     * @param neighborhood the neighborhood to get alerts for
     * @return the list of alerts associated with the given neighborhood
     */
    public List<Alert> getAlertsForNeighborhood(Neighborhood neighborhood) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Display the details for an alert on selection
     * @return true if the selection succeeds, false otherwise
     */
    public boolean onAlertSelect() {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
}
