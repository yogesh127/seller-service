package model;

public class SellerFactory implements EntityFactory {
    @Override
    public Entity createEntity(String name) {
        return new Seller(name);
    }
}
