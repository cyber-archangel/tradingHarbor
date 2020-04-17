package main.ships;

public class HeavyShipFactory implements ShipFactory {
    @Override
    public Ship createShip() {
        return new HeavyShip();
    }
}
