package model;

import java.util.HashSet;
import java.util.Set;

public class User extends Entity implements Observer {
    private String name;
    private Set<Seller> subscriptions;

    public User(String name) {
        super(name);
        this.name = name;
        this.subscriptions = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public synchronized void subscribe(Seller seller) {
        subscriptions.add(seller);
    }

    public synchronized void unsubscribe(Seller seller) {
        subscriptions.remove(seller);
    }

    public synchronized Set<Seller> getSubscriptions() {
        return subscriptions;
    }

    @Override
    public void update(Product product) {
        System.out.println("User " + name + " received update for product: " + product.getName());
    }
}
