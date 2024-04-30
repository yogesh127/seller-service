package service;

import model.Product;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RatingSortingStrategy implements SortingStrategy{
    @Override
    public List<Product> sort(List<Product> products) {
        Collections.sort(products, Comparator.comparing(Product::getRating).reversed());
        return products;
    }
}
