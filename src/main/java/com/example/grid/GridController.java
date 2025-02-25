package com.example.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class GridController implements Runnable {
    private List<EnergyProducer> producers;
    private List<EnergyConsumer> consumers;
    private AtomicBoolean running;

    public GridController() {
        this.producers = new ArrayList<>();
        this.consumers = new ArrayList<>();
        this.running = new AtomicBoolean(false);
    }

    public void addProducer(EnergyProducer producer) {
        producers.add(producer);
    }

    public void addConsumer(EnergyConsumer consumer) {
        consumers.add(consumer);
    }

    public double getTotalProduction() {
        return producers.stream()
                .mapToDouble(EnergyNode::getCurrentLoad)
                .sum();
    }

    public double getTotalConsumption() {
        return consumers.stream()
                .mapToDouble(EnergyNode::getCurrentLoad)
                .sum();
    }

    private double calculateLoadBalance() {
        double totalProduction = producers.stream()
                .mapToDouble(EnergyNode::getCurrentLoad)
                .sum();
        double totalConsumption = consumers.stream()
                .mapToDouble(EnergyNode::getCurrentLoad)
                .sum();
        return totalProduction - totalConsumption;
    }

    private List<String> detectFaults() {
        List<String> faults = new ArrayList<>();
        for (EnergyProducer producer : producers) {
            if (!producer.isOperational()) {
                faults.add("Producer " + producer.getNodeId() + " is offline");
            }
        }
        return faults;
    }

    private void optimizeDistribution() {
        double balance = calculateLoadBalance();
        if (balance < 0) {
            System.out.printf("Warning: Power deficit of %.2f units%n", Math.abs(balance));
        } else if (balance > 0) {
            System.out.printf("Info: Power surplus of %.2f units%n", balance);
        }
    }

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

    public void stop() {
        running.set(false);
    }
}