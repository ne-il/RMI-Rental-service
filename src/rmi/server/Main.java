package rmi.server;

import java.io.UncheckedIOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

/**
 * Created by nakaze on 03/01/18.
 */
public class Main {
    private static Registry registry;

    static {
        try {
            registry = LocateRegistry.createRegistry(1080);
        } catch (RemoteException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void main(String[] args) {
        try {
            CatalogueManager catalogueManager = new CatalogueManagerImpl();
            registry.rebind("catalogueManager", catalogueManager);


            AuthentificatorImpl internalAuthentificator = new AuthentificatorImpl();
            internalAuthentificator.createStudent("Lena", "Oxton", "Tracer", "CheersLove");
            internalAuthentificator.createTeacher("Amélie", "Guillard", "Widowmaker", "UneBalleUnMort");
            internalAuthentificator.createTeacher("Mei", "Xiao-Ling", "Mei", "Ameizing");
            internalAuthentificator.createTeacher("Neil", "Anteur", "a", "a");

            registry.rebind("internalAuthentificator", internalAuthentificator);

            AuthentificatorImpl externalAuthentificator = new AuthentificatorImpl();
            externalAuthentificator.createNormalUser("Lena", "Oxton", "Tracer", "CheersLove");
            externalAuthentificator.createNormalUser("Amélie", "Guillard", "Widowmaker", "UneBalleUnMort");
            externalAuthentificator.createNormalUser("Mei", "Xiao-Ling", "Mei", "Ameizing");
            externalAuthentificator.createNormalUser("Neil", "Anteur", "a", "a");
            registry.rebind("externalAuthentificator", externalAuthentificator);

//            User neil = internalAuthentificator.tryLog("Widowmaker", "UneBalleUnMort");
            User neil = internalAuthentificator.tryLog("a", "a");

            neil.addProductToCatalogue(catalogueManager, "Overwatch", "MMO FPS", 4);
            neil.addProductToCatalogue(catalogueManager, "Doom", "jeux avec des monstre",4);
            neil.addProductToCatalogue(catalogueManager, "Wolfenstein", "jeu de nazi",4);
            neil.addProductToCatalogue(catalogueManager, "Prince Of Persia", "prince BG avec Farah la magnifique",4);
            neil.addProductToCatalogue(catalogueManager, "Halo", "sur xbox",4);

            List<Product> catalogue = catalogueManager.getProducts();

//            for (Product p: catalogue) {
//                neil.rentProduct(p);
//            }

//            Product p = catalogue.get(0);
//            neil.modifyProductDescription(p, "Modify la description");
//            neil.returnProduct(p, 5, "bite");


//            neil.returnProduct(catalogue.get(3), 5, "bite");
            User user1 = internalAuthentificator.tryLog("Widowmaker", "UneBalleUnMort");

            user1.addProductToCatalogue(catalogueManager, "Overwatch", "A 100-times acclaimed game.", 3000);
            Product p = catalogueManager.getProducts().get(0);
            user1.modifyProductDescription(p, "Test");

            System.out.println("Objects binded, server now running...");
        } catch (RemoteException e) {
            throw new UncheckedIOException(e);
        }
    }
}
