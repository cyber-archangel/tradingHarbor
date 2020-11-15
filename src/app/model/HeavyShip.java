package app.model;

public class HeavyShip implements Ship {

    private final int consignment = 100;

    @Override
    public void uploadConsignment() throws InterruptedException {
        System.out.println("Unloading the consignment of the heavy ship...");
        Thread.sleep(3000);
        System.out.println(consignment + " units of consignment were unloaded!");
    }

    @Override
    public int getConsignment() {
        return consignment;
    }
}