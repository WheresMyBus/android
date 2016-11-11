package com.wheresmybus;

import android.util.Pair;

/**
 * Mock class to test class with Pair
 * Created by lidav on 11/11/2016.
 */

public class MockPair extends Pair<Double,Double>{
    public final double first = 1.0;
    public final double second = 0.0;
    private MockPair(double first, double second) {
        super(first, second);
    }

    public MockPair() {
        this(1.0, 0.0);
    }
}
