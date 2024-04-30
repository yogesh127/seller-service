package model;

import java.util.ArrayList;
import java.util.List;

public class Seller extends Entity{
    private String name;
    private List<Observer> observers;
    private List<Product> products;

    public Seller(String name) {
        super(name);
        this.name = name;
        this.observers = new ArrayList<>();
        this.products = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public synchronized void addObserver(Observer observer) {
        observers.add(observer);
    }

    public synchronized void uploadProduct(Product product) {
        products.add(product);
        notifyObservers(product);
    }

    private void notifyObservers(Product product) {
        for (Observer observer : observers) {
            observer.update(product);
        }
    }

    public synchronized List<Product> getProducts() {
        return products;
    }
}
