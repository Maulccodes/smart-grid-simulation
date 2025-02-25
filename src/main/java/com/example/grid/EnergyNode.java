package com.example.grid;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class EnergyNode {
    protected String nodeId;
    protected double capacity;
    protected double currentLoad;
    protected boolean running;
    protected Lock lock;

    public EnergyNode(String nodeId, double capacity) {
        this.nodeId = nodeId;
        this.capacity = capacity;
        this.currentLoad = 0;
        this.running = false;
        this.lock = new ReentrantLock();
    }

    public abstract void update();

    // Getters
    public String getNodeId() { return nodeId; }
    public double getCurrentLoad() { return currentLoad; }
    public double getCapacity() { return capacity; }
}