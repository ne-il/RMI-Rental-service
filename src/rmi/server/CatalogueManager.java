package rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * A class representing the whole catalogue of products.
 */
public interface CatalogueManager extends Remote {
    /**
     * Add a product in the catalogue.
     *
     * @param name        Name of the product
     * @param description Description of the product
     * @param price       Price of the product
     * @param owner       The user who owns the added product
     * @return the created product
     * @throws RemoteException
     */
    Product addProduct(String name, String description, int price, User owner) throws RemoteException;

    /**
     * Removes a product from the catalogue.
     *
     * @param p Product to be removed.
     * @throws RemoteException
     */
    void removeProduct(Product p) throws RemoteException;

    /**
     * Get the list of products in the catalogue.
     *
     * @return The wanted list
     * @throws RemoteException
     */
    ArrayList<Product> getProducts() throws RemoteException;

    /**
     * Get the list of sellable products in the catalogue
     *
     * @return The wanted list
     * @throws RemoteException
     */
    ArrayList<Product> getSellableProducts() throws RemoteException;

    /**
     * Find a product in the catalogue with his id.
     * @param id The id of the searched product
     * @return The product if found, otherwise <b>null</b>.
     * @throws RemoteException
     */
    Product getProductById(int id) throws RemoteException;
}
