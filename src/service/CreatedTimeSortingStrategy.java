package service;

import model.Product;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CreatedTimeSortingStrategy implements SortingStrategy{
    @Override
    public List<Product> sort(List<Product> products) {
        Collections.sort(products, Comparator.comparing(Product::getCreatedAt));
        return products;
    }
}
