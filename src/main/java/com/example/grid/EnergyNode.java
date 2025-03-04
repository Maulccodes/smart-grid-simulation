package com.example.grid;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Abstract base class for all energy nodes in the smart grid system.
 * Provides common functionality for both producers and consumers.
 * Implements thread-safe operations using ReentrantLock.
 */
public abstract class EnergyNode {
    /** Unique identifier for the node */
    protected String nodeId;
    /** Maximum power capacity in MW */
    protected double capacity;
    /** Current power load/production in MW */
    protected double currentLoad;
    /** Operational status flag */
    protected boolean running;
    /** Thread synchronization lock for safe updates */
    protected Lock lock;

    /**
     * Creates a new energy node with specified parameters.
     * @param nodeId Unique identifier for this node
     * @param capacity Maximum power capacity in MW
     */
    public EnergyNode(String nodeId, double capacity) {
        this.nodeId = nodeId;
        this.capacity = capacity;
        this.currentLoad = 0;
        this.running = false;
        this.lock = new ReentrantLock();
    }

    /**
     * Updates the node's current power load/production.
     * Must be implemented by concrete producer/consumer classes.
     */
    public abstract void update();

    /** @return The node's unique identifier */
    public String getNodeId() { return nodeId; }
    /** @return The current power load/production in MW */
    public double getCurrentLoad() { return currentLoad; }
    /** @return The maximum power capacity in MW */
    public double getCapacity() { return capacity; }
}