package com.example.grid;

import java.util.Random;

public class EnergyProducer extends EnergyNode {
    private double failureRate;
    private boolean operational;
    private Random random;

    public EnergyProducer(String nodeId, double capacity, double failureRate) {
        super(nodeId, capacity);
        this.failureRate = failureRate;
        this.operational = true;
        this.random = new Random();
    }

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

    public boolean isOperational() {
        return operational;
    }
}