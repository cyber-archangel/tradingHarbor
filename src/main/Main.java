package main;

import java.util.*;
import java.util.concurrent.*;
import main.ships.*;
import javax.swing.Timer;

public class Main {

    private final Harbor harbor = new Harbor();
    private final String[] shipDrawings = {"light", "average", "heavy"};
    private final Random random = new Random();
    private boolean readiness = true;

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        System.out.println("A new profitable day has begun ...");

        Timer day = new Timer(10, event -> readiness = !readiness);

        day.start();

        try {
            shipsQueueMaking();
            while (readiness) {
                synchronized (day) {
                    day.wait();
                }
            }
        } catch (InterruptedException | ExecutionException exception) {
            exception.printStackTrace(System.out);
        }

        System.out.println("The day is over! Profit: " + harbor.getTotalProfit());
    }

    private synchronized void shipsQueueMaking() throws ExecutionException, InterruptedException {
        ArrayList<Thread> ships = launchShips();
        ships.forEach(this::addToQueue);
    }

    private ArrayList<Thread> launchShips() throws ExecutionException, InterruptedException {
        FutureTask<ArrayList<Thread>> ships = new FutureTask<>(new ShipCreator());
        new Thread(ships).start();
        return ships.get();
    }

    private synchronized void addToQueue(Thread ship) {
        if(harbor.isFreePlacesInHarbor()) {
            harbor.takePlace(ship);
            letShipsComeIn(harbor.getShips());
        } else {
            ship.interrupt();
            System.out.println("The ship sailed to another harbor...");
        }
    }

    private void letShipsComeIn(ArrayList<Thread> ships) {
        ships.stream().filter(ship -> ship.getState() != Thread.State.RUNNABLE && ship.getState() != Thread.State.BLOCKED).forEach(Thread::start);
    }

    private ShipFactory createShipByType(String type) {
        if(type.equalsIgnoreCase("light"))
            return new LightShipFactory();
        else if(type.equalsIgnoreCase("average"))
            return new AverageShipFactory();
        else if(type.equalsIgnoreCase("heavy"))
            return new HeavyShipFactory();
        else
            throw new RuntimeException();
    }

    private class ShipCreator implements Callable<ArrayList<Thread>> {
        @Override
        public ArrayList<Thread> call() throws InterruptedException {
            ArrayList<Thread> ships = new ArrayList<>();
            while (readiness) {
                if(ships.size() > 5)
                    Thread.sleep(3000);
                ships.add(new Thread(() -> {
                    try {
                        harbor.serveTheShip(new FutureTask<>(new createShipCallable()));
                    } catch (InterruptedException | ExecutionException exception) {
                        exception.printStackTrace();
                    }
                }));
            }
            return ships;
        }
    }

    private class createShipCallable implements Callable<Ship> {
        @Override
        public Ship call() {
            ShipFactory shipFactory = createShipByType(shipDrawings[random.nextInt(3)]);
            return shipFactory.createShip();
        }
    }
}