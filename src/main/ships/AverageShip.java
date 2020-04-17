package main.ships;

public class AverageShip implements Ship {

    private final int consignment = 50;

    @Override
    public void uploadConsignment() throws InterruptedException {
        System.out.println("Unloading the consignment of the average ship...");
        Thread.sleep(3000);
        System.out.println(consignment + " units of consignment were unloaded!");
    }

    @Override
    public int getConsignment() {
        return consignment;
    }
}
