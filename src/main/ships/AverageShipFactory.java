package main.ships;

public class AverageShipFactory implements ShipFactory {
    @Override
    public Ship createShip() {
        return new AverageShip();
    }
}
