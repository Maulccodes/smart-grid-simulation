package com.example.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Main controller class for the smart grid system.
 * Manages energy producers and consumers, monitors grid status,
 * and handles load balancing and fault detection.
 */
public class GridController implements Runnable {
    /** List of all energy producers in the grid */
    private List<EnergyProducer> producers;
    /** List of all energy consumers in the grid */
    private List<EnergyConsumer> consumers;
    /** Thread-safe boolean flag for controlling the simulation */
    private AtomicBoolean running;

    /**
     * Initializes a new grid controller with empty lists of producers and consumers.
     */
    public GridController() {
        this.producers = new ArrayList<>();
        this.consumers = new ArrayList<>();
        this.running = new AtomicBoolean(false);
    }

    /**
     * Adds a new energy producer to the grid.
     * @param producer The energy producer to add
     */
    public void addProducer(EnergyProducer producer) {
        producers.add(producer);
    }

    /**
     * Adds a new energy consumer to the grid.
     * @param consumer The energy consumer to add
     */
    public void addConsumer(EnergyConsumer consumer) {
        consumers.add(consumer);
    }

    /**
     * Calculates the total current power production across all producers.
     * @return Total power production in MW
     */
    public double getTotalProduction() {
        return producers.stream()
                .mapToDouble(EnergyNode::getCurrentLoad)
                .sum();
    }

    /**
     * Calculates the total current power consumption across all consumers.
     * @return Total power consumption in MW
     */
    public double getTotalConsumption() {
        return consumers.stream()
                .mapToDouble(EnergyNode::getCurrentLoad)
                .sum();
    }

    /**
     * Calculates the current load balance in the grid.
     * @return Difference between total production and consumption (positive means surplus)
     */
    private double calculateLoadBalance() {
        double totalProduction = producers.stream()
                .mapToDouble(EnergyNode::getCurrentLoad)
                .sum();
        double totalConsumption = consumers.stream()
                .mapToDouble(EnergyNode::getCurrentLoad)
                .sum();
        return totalProduction - totalConsumption;
    }

    /**
     * Detects and reports any faults in the grid system.
     * Currently checks for offline producers.
     * @return List of fault messages
     */
    private List<String> detectFaults() {
        List<String> faults = new ArrayList<>();
        for (EnergyProducer producer : producers) {
            if (!producer.isOperational()) {
                faults.add("Producer " + producer.getNodeId() + " is offline");
            }
        }
        return faults;
    }

    /**
     * Optimizes power distribution by monitoring load balance.
     * Prints warnings for power deficits and surpluses.
     */
    private void optimizeDistribution() {
        double balance = calculateLoadBalance();
        if (balance < 0) {
            System.out.printf("Warning: Power deficit of %.2f units%n", Math.abs(balance));
        } else if (balance > 0) {
            System.out.printf("Info: Power surplus of %.2f units%n", balance);
        }
    }

    /**
     * Main control loop for the grid system.
     * Updates all nodes, performs grid management, and handles fault detection.
     * Runs continuously until stopped.
     */
    @Override
    public void run() {
        running.set(true);
        while (running.get()) {
            // Update all nodes
            producers.forEach(EnergyNode::update);
            consumers.forEach(EnergyNode::update);

            // Perform grid management
            optimizeDistribution();
            List<String> faults = detectFaults();
            if (!faults.isEmpty()) {
                System.out.println("Detected faults: " + String.join(", ", faults));
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Stops the grid controller's main loop.
     */
    public void stop() {
        running.set(false);
    }
}