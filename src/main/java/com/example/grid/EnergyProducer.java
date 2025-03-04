package com.example.grid;

import java.util.Random;

/**
 * Represents an energy producer in the smart grid system.
 * Simulates power generation with random failures and output variations.
 * Examples include solar farms, wind farms, and power plants.
 */
public class EnergyProducer extends EnergyNode {
    /** Probability of failure per update cycle (0.0 to 1.0) */
    private double failureRate;
    /** Current operational status of the producer */
    private boolean operational;
    /** Random number generator for production variations and failures */
    private Random random;

    /**
     * Creates a new energy producer with specified parameters.
     * @param nodeId Unique identifier for this producer
     * @param capacity Maximum power production capacity in MW
     * @param failureRate Probability of failure per update cycle (0.0 to 1.0)
     */
    public EnergyProducer(String nodeId, double capacity, double failureRate) {
        super(nodeId, capacity);
        this.failureRate = failureRate;
        this.operational = true;
        this.random = new Random();
    }

    /**
     * Updates the current power production based on operational status.
     * Simulates random failures and production variations.
     * When operational, produces between 60% and 100% of capacity.
     * When failed, produces no power (0).
     * Thread-safe method using lock from parent class.
     */
    @Override
    public void update() {
        lock.lock();
        try {
            if (random.nextDouble() < failureRate) {
                operational = !operational;
            }
            
            if (operational) {
                currentLoad = capacity * (0.6 + random.nextDouble() * 0.4);
            } else {
                currentLoad = 0;
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * @return Current operational status of the producer
     */
    public boolean isOperational() {
        return operational;
    }
}