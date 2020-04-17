package main.ships;

public interface Ship {
    void uploadConsignment() throws InterruptedException;
    int getConsignment();
}
