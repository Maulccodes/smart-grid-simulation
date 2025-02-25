package com.example.grid;

import java.time.LocalTime;
import java.util.Random;

public class EnergyConsumer extends EnergyNode {
    private final int peakStartHour;
    private final int peakEndHour;
    private Random random;

    public EnergyConsumer(String nodeId, double capacity, int peakStartHour, int peakEndHour) {
        super(nodeId, capacity);
        this.peakStartHour = peakStartHour;
        this.peakEndHour = peakEndHour;
        this.random = new Random();
    }

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