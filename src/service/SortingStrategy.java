package service;

import model.Product;

import java.util.List;

public interface SortingStrategy {
    List<Product> sort(List<Product> products);

}
