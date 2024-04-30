import exception.EntityAlreadyExistsException;
import exception.EntityNotFoundException;
import model.Product;
import model.Seller;
import model.SellerFactory;
import model.UserFactory;
import service.RatingSortingStrategy;
import service.Platform;
import service.SortingStrategy;

import java.util.Date;
import java.util.List;

public class Main {

    public static void ConcurrencyCheck()
    {

        Platform sellerService = new Platform();
        try {
        sellerService.createEntity("Seller1", new SellerFactory());
        sellerService.createEntity("User1", new UserFactory());


            sellerService.subscribeUserToSeller("User1", "Seller1");

            Thread uploadThread1 = new Thread(() -> {
                for (int i = 0; i < 3; i++) {
                    Seller seller = (Seller) sellerService.getEntities().get("Seller1");
                    seller.uploadProduct(new Product("Product " + i, i + 1, new Date()));
                }
            });

            Thread uploadThread2 = new Thread(() -> {
                for (int i = 3; i < 6; i++) {
                    Seller seller = (Seller) sellerService.getEntities().get("Seller1");
                    seller.uploadProduct(new Product("Product " + i, i + 1, new Date()));
                }
            });

            Thread unsubscribeThread = new Thread(() -> {
                try {
                    sellerService.unsubscribeUserFromSeller("User1", "Seller1");
                } catch (EntityNotFoundException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            });

            uploadThread1.start();
            uploadThread2.start();
            unsubscribeThread.start();

            uploadThread1.join();
            uploadThread2.join();
            unsubscribeThread.join();
            SortingStrategy sortingStrategy = new RatingSortingStrategy();

            List<Product> sortedFeed = sellerService.getSortedFeedForUser("User1", sortingStrategy);
            if (sortedFeed != null) {
                System.out.println("Sorted Feed for User1:");
                for (Product product : sortedFeed) {
                    System.out.println("Product Name: " + product.getName() + ", Rating: " + product.getRating());
                }
            }
        } catch (EntityNotFoundException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (EntityAlreadyExistsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        Platform sellerService = new Platform();

        try {
            sellerService.createEntity("Seller1", new SellerFactory());
            sellerService.createEntity("Seller2", new SellerFactory());
            sellerService.createEntity("User1", new UserFactory());
            sellerService.createEntity("User2", new UserFactory());

            sellerService.subscribeUserToSeller("User1", "Seller1");
            sellerService.subscribeUserToSeller("User1", "Seller2");

            Seller seller1 = (Seller) sellerService.getEntities().get("Seller1");
            seller1.uploadProduct(new Product("Product1", 4, new Date()));
            seller1.uploadProduct(new Product("Product2", 5, new Date()));

            Seller seller2 = (Seller) sellerService.getEntities().get("Seller2");
            seller2.uploadProduct(new Product("Product3", 3, new Date()));
            seller2.uploadProduct(new Product("Product4", 2, new Date()));
            SortingStrategy sortingStrategy = new RatingSortingStrategy();
            List<Product> sortedFeed = sellerService.getSortedFeedForUser("User1", sortingStrategy);
            if (sortedFeed != null) {
                System.out.println("Sorted Feed for User1:");
                for (Product product : sortedFeed) {
                    System.out.println("Product Name: " + product.getName() + ", Rating: " + product.getRating());
                }
            }

            ConcurrencyCheck();
        }
        catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
