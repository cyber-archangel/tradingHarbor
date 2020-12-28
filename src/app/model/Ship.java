package app.model;

public interface Ship {

    void uploadConsignment() throws InterruptedException;

    int getConsignment();
}