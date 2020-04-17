package main;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import main.ships.*;

public class Harbor {

    private volatile List<Callable<Ship>> ships = new ArrayList<>();
    private int totalProfit = 0;

    public synchronized void serveTheShip(FutureTask<Ship> ship) throws InterruptedException, ExecutionException {
        System.out.println();
        proceedShip(ship);
    }

    public void proceedShip(FutureTask<Ship> ship) throws InterruptedException, ExecutionException {
        System.out.println("A new ship docked at the marina...");
        ship.get().uploadConsignment();
        totalProfit += ship.get().getConsignment();
        freeUpDock();
    }

    private boolean isFreePlacesInHarbor() {
        return ships.size() < 5;
    }

    private void takePlace(Callable<Ship> ship) {
        ships.add(ship);
    }

    private void freeUpDock() {
        ships.remove(ships.size() - 1);
    }
}