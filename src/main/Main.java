package main;

import java.util.*;
import java.util.concurrent.*;

import main.ships.*;

public class Main {

    private final Harbor harbor = new Harbor();
    private final String[] shipDrawings = {"light", "average", "heavy"};
    private final ScheduledExecutorService dayTimer = Executors.newScheduledThreadPool(1);
    private final Random random = new Random();

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        System.out.println("A new profitable day has begun ...");

        dayTimer.scheduleWithFixedDelay(() -> {
            if(System.currentTimeMillis() == 15000)
                shipQueueMaking(false);
        }, 0, 1, TimeUnit.SECONDS);

        shipQueueMaking(true);

        if(dayTimer.isTerminated())
            System.out.println("The day is over!");
    }

    private synchronized void shipQueueMaking(boolean readiness) {
        ArrayList<Callable<Ship>> ships = launchShips(readiness);
    }

    private ArrayList<Callable<Ship>> launchShips(boolean readiness) {
        ArrayList<Callable<Ship>> ships = new ArrayList<>();
        while(readiness) {
            //Callable<Ship> ship = new ShipCallable(harbor::);
            //FutureTask<Ship> shipFutureTask = new FutureTask<>(ship);
        }
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

    private class ShipCallable implements Callable<Ship> {
        @Override
        public Ship call() {
            ShipFactory shipFactory = createShipByType(shipDrawings[random.nextInt(2)]);
            return shipFactory.createShip();
        }
    }
}