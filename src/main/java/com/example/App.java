package com.example;

import com.example.grid.*;

public class App {
    public static void main(String[] args) {
        GridController grid = new GridController();
        GridVisualizer visualizer = new GridVisualizer();

        // Add energy producers
        grid.addProducer(new EnergyProducer("solar_farm_1", 1000, 0.01));
        grid.addProducer(new EnergyProducer("wind_farm_1", 800, 0.01));
        grid.addProducer(new EnergyProducer("power_plant_1", 2000, 0.005));

        // Add energy consumers
        grid.addConsumer(new EnergyConsumer("residential_area_1", 500, 9, 17));
        grid.addConsumer(new EnergyConsumer("industrial_zone_1", 1500, 8, 18));
        grid.addConsumer(new EnergyConsumer("commercial_district_1", 1000, 9, 17));

        // Create visualization thread
        Thread visualThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                visualizer.displayGridStatus(
                    grid.getTotalProduction(),
                    grid.getTotalConsumption()
                );
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        // Start the grid simulation in a separate thread
        Thread gridThread = new Thread(grid);
        gridThread.start();
        visualThread.start();

        // Add shutdown hook for clean termination
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down the simulation...");
            grid.stop();
            visualThread.interrupt();
            try {
                gridThread.join();
                visualThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }));

        // Keep the main thread alive
        try {
            gridThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
