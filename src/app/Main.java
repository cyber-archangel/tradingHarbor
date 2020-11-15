package app;

import java.util.*;
import java.util.concurrent.*;

import app.model.*;

public class Main {

    private final Harbor harbor = new Harbor();

    private final String[] shipDrawings = {"light", "average", "heavy"};

    private final Random random = new Random();

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {

        System.out.println("A new profitable day has begun ...");

        Timer day = new Timer();

        day.schedule(new TimerTask() {

            @Override
            public void run() {
                System.out.println("The day is over! Profit: " + harbor.getTotalProfit());
                System.exit(0);
            }
        }, 30000);

        try {
            shipsQueueMaking();
        } catch (InterruptedException | ExecutionException exception) {
            exception.printStackTrace(System.out);
        }
    }

    private synchronized void shipsQueueMaking() throws ExecutionException, InterruptedException {
        launchShips().forEach(this::addToQueue);
    }

    private ArrayList<Thread> launchShips() throws ExecutionException, InterruptedException {
        FutureTask<ArrayList<Thread>> ships = new FutureTask<>(new ShipCreator());
        new Thread(ships).start();
        return ships.get();
    }

    private synchronized void addToQueue(Thread ship) {
        if (harbor.isFreePlacesInHarbor()) {
            harbor.takePlace(ship);
            letShipsComeIn(harbor.getShips());
        } else {
            ship.interrupt();
            System.out.println("The ship sailed to another harbor...");
        }
    }

    private void letShipsComeIn(ArrayList<Thread> ships) {
        ships.stream().filter(ship -> ship.getState() != Thread.State.RUNNABLE && ship.getState() != Thread.State.BLOCKED && ship.getState() != Thread.State.WAITING).forEach(Thread::start);
    }

    private ShipFactory createShipByType(String type) {

        if (type.equalsIgnoreCase("light"))
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
        public ArrayList<Thread> call() {

            ArrayList<Thread> ships = new ArrayList<>();

            for (int i = 0; i < 20; ++i) {

                ships.add(new Thread(() -> {
                    try {
                        harbor.serveTheShip(new FutureTask<>(new ShipCreatorCallable()));
                    } catch (InterruptedException | ExecutionException exception) {
                        exception.printStackTrace();
                    }
                }));
            }

            return ships;
        }
    }

    private class ShipCreatorCallable implements Callable<Ship> {

        @Override
        public Ship call() {
            return createShipByType(shipDrawings[random.nextInt(3)]).createShip();
        }
    }
}