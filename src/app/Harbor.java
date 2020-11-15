package app;

import java.util.*;
import java.util.concurrent.*;

import app.model.*;

public class Harbor {

    private volatile ArrayList<Thread> ships = new ArrayList<>();

    private int totalProfit = 0;

    public synchronized void serveTheShip(FutureTask<Ship> ship) throws InterruptedException, ExecutionException {
        System.out.println();
        proceedShip(ship);
        freeUpDock();
        System.out.println();
    }

    public void proceedShip(FutureTask<Ship> ship) throws InterruptedException, ExecutionException {
        System.out.println("A new ship docked at the marina...");
        new Thread(ship).start();
        ship.get().uploadConsignment();
        totalProfit += ship.get().getConsignment();
        System.out.println("The ship set sail!");
    }

    public boolean isFreePlacesInHarbor() {
        return ships.size() < 15;
    }

    public int getTotalProfit() {
        return totalProfit;
    }

    public void takePlace(Thread ship) {
        ships.add(ship);
    }

    private void freeUpDock() {
        ships.remove(ships.size() - 1);
    }

    public ArrayList<Thread> getShips() {
        return ships;
    }
}