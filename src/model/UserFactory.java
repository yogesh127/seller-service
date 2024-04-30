package model;

public class UserFactory implements EntityFactory{
    @Override
    public Entity createEntity(String name) {
        return new User(name);
    }
}
