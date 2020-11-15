package app.model;

public class LightShip implements Ship {

    private final int consignment = 25;

    @Override
    public void uploadConsignment() throws InterruptedException {
        System.out.println("Unloading the consignment of the light ship...");
        Thread.sleep(1000);
        System.out.println(consignment + " units of consignment were unloaded!");
    }

    @Override
    public int getConsignment() {
        return consignment;
    }
}