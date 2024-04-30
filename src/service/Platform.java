package service;

import exception.EntityAlreadyExistsException;
import exception.EntityNotFoundException;
import model.Entity;
import model.Product;
import model.Seller;
import model.User;
import model.EntityFactory;

import java.util.*;

public class Platform {

    private Map<String, Entity> entities; // Key -> username, sellerName {Seller, User}
    private SortingStrategy sortingStrategy;

    public Platform() {
        this.entities = new HashMap<>();
    }

    public synchronized void createEntity(String name, EntityFactory factory) throws EntityAlreadyExistsException {
        if (entities.containsKey(name)) {
            throw new EntityAlreadyExistsException("Entity '" + name + "' already exists.");
        }
        entities.put(name, factory.createEntity(name));
    }

    public synchronized void deleteEntity(String name) throws EntityNotFoundException {
        if (!entities.containsKey(name)) {
            throw new EntityNotFoundException("Entity '" + name + "' not found.");
        }
        entities.remove(name);
    }

    public synchronized void subscribeUserToSeller(String userName, String sellerName) throws EntityNotFoundException {
        Entity userEntity = entities.get(userName);
        Entity sellerEntity = entities.get(sellerName);
        if (!(userEntity instanceof User) || !(sellerEntity instanceof Seller)) {
            throw new EntityNotFoundException("User or seller not found.");
        }
        User user = (User) userEntity;
        Seller seller = (Seller) sellerEntity;
        user.subscribe(seller);
        seller.addObserver(user);
    }

    public synchronized void unsubscribeUserFromSeller(String userName, String sellerName) throws EntityNotFoundException {
        Entity userEntity = entities.get(userName);
        Entity sellerEntity = entities.get(sellerName);
        if (!(userEntity instanceof User) || !(sellerEntity instanceof Seller)) {
            throw new EntityNotFoundException("User or seller not found.");
        }
        User user = (User) userEntity;
        Seller seller = (Seller) sellerEntity;
        user.unsubscribe(seller);
    }

    public List<Product> getSortedFeedForUser(String userName, SortingStrategy sortingStrategy ) throws EntityNotFoundException {
        Entity userEntity = entities.get(userName);
        if (!(userEntity instanceof User)) {
            throw new EntityNotFoundException("User not found.");
        }
        User user = (User) userEntity;
        List<Product> feed = new ArrayList<>();
        for (Seller seller : user.getSubscriptions()) {
            feed.addAll(seller.getProducts());
        }
        return sortingStrategy.sort(feed);
    }
    public synchronized Map<String, Entity> getEntities() {
        return entities;
    }
}
