package com.example.grid;

import java.time.LocalTime;
import java.util.Random;

/**
 * Represents an energy consumer in the smart grid system.
 * Simulates power consumption patterns with peak and off-peak hours.
 * Examples include residential areas, industrial zones, and commercial districts.
 */
public class EnergyConsumer extends EnergyNode {
    /** Hour when peak consumption period starts (24-hour format) */
    private final int peakStartHour;
    /** Hour when peak consumption period ends (24-hour format) */
    private final int peakEndHour;
    /** Random number generator for consumption variations */
    private Random random;

    /**
     * Creates a new energy consumer with specified parameters.
     * @param nodeId Unique identifier for this consumer
     * @param capacity Maximum power consumption capacity in MW
     * @param peakStartHour Start hour of peak consumption period (24-hour format)
     * @param peakEndHour End hour of peak consumption period (24-hour format)
     */
    public EnergyConsumer(String nodeId, double capacity, int peakStartHour, int peakEndHour) {
        super(nodeId, capacity);
        this.peakStartHour = peakStartHour;
        this.peakEndHour = peakEndHour;
        this.random = new Random();
    }

    /**
     * Updates the current power consumption based on time of day and random variations.
     * During peak hours, base load is 70% of capacity.
     * During off-peak hours, base load is 30% of capacity.
     * Random variations of ±10% are applied to simulate real-world fluctuations.
     * Thread-safe method using lock from parent class.
     */
    @Override
    public void update() {
        lock.lock();
        try {
            int currentHour = LocalTime.now().getHour();
            boolean isPeakHour = currentHour >= peakStartHour && currentHour <= peakEndHour;
            
            double baseLoad = isPeakHour ? 0.7 : 0.3;
            double variation = -0.1 + random.nextDouble() * 0.2;
            currentLoad = (baseLoad + variation) * capacity;
        } finally {
            lock.unlock();
        }
    }
}