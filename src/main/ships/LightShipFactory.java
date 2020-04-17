package main.ships;

public class LightShipFactory implements ShipFactory {
    @Override
    public Ship createShip() {
        return new LightShip();
    }
}
