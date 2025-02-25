package com.example.grid;

public class GridVisualizer {
    private static final int CHART_WIDTH = 50;
    
    public void displayGridStatus(double totalProduction, double totalConsumption) {
        clearConsole();
        System.out.println("\n=== Smart Grid Status ===");
        
        // Production bar
        System.out.print("Production  [");
        int prodBar = (int)((totalProduction / 4000.0) * CHART_WIDTH);
        printBar(prodBar);
        System.out.printf("] %.2f MW\n", totalProduction);
        
        // Consumption bar
        System.out.print("Consumption [");
        int consBar = (int)((totalConsumption / 4000.0) * CHART_WIDTH);
        printBar(consBar);
        System.out.printf("] %.2f MW\n", totalConsumption);
        
        // Balance
        double balance = totalProduction - totalConsumption;
        System.out.printf("\nGrid Balance: %.2f MW\n", balance);
    }
    
    private void printBar(int length) {
        for (int i = 0; i < CHART_WIDTH; i++) {
            System.out.print(i < length ? "█" : " ");
        }
    }
    
    private void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}