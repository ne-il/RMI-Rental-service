package rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * A class representing an user.
 * <p>
 * An user has a full name (first name and last name), an ID, and a priority for renting.
 * He can add products and modify/remove products he have added. He can rent products and return them.
 */
public interface User extends Remote {
    int getUserId() throws RemoteException;

    String getFirstName() throws RemoteException;

    String getLastName() throws RemoteException;

    int getPriority() throws RemoteException;

    /**
     * Get the list of the products this user is queueing for.
     * @return That list
     * @throws RemoteException
     */
    ArrayList getQueueingProducts() throws RemoteException;

    /**
     * @return the total number of products rented by this user
     * @throws RemoteException
     */
    int getRentCount() throws RemoteException;

    /**
     * For the sorting function, to know which user has more priority than the other.
     *
     * @param u The user to compare this one to.
     * @return A negative, zero or positive value for respectively a lesser, same, greater priority than the user u.
     */
    int compareTo(User u) throws RemoteException;

    /**
     * Make this user add a product to the general catalogue.
     *
     * @param catalogueManager Catalogue where this product will be added to.
     * @param name             Name of the product
     * @param description      Description of the product
     * @param price            Price of the product
     * @throws RemoteException
     */
    void addProductToCatalogue(CatalogueManager catalogueManager, String name, String description, int price) throws RemoteException;

    /**
     * Make this user modify a description of a product. The product must be his.
     *
     * @param p              The product with the non-desired description.
     * @param newDescription The new description for the given product.
     * @throws RemoteException
     */
    void modifyProductDescription(Product p, String newDescription) throws RemoteException;

    /**
     * Make this user rent a product from the catalogue.
     *
     * @param p Rented product
     * @throws RemoteException
     */
    void rentProduct(Product p) throws RemoteException;

    /**
     * If the user is queueing for the product P, he will be no more queueing for it.
     *
     * @param p The product to be unregistered from
     * @throws RemoteException
     */
    void unregisterFromProduct(Product p) throws RemoteException;

    /**
     * Make this user remove a product from the catalogue. The product must be his.
     * TODO This doesn't work. Explained in the implementation.
     *
     * @param catalogueManager Catalogue where this product will be removed from.
     * @param p                The non-desired product
     * @throws RemoteException
     */
    void removeProductFromCatalogue(CatalogueManager catalogueManager, Product p) throws RemoteException;

    /**
     * Make this user return a product he rented, and review it.
     *
     * @param p       The returned product
     * @param rating  The rating given for this product
     * @param comment The comment given
     * @throws RemoteException
     */
    void returnProduct(Product p, int rating, String comment) throws RemoteException;

    /**
     * @return the list of added products
     * @throws RemoteException
     */
    ArrayList<Product> getAddedProducts() throws RemoteException;

    /**
     * Get the list of this user rented products.
     *
     * @return The wanted list
     * @throws RemoteException
     */
    ArrayList<Product> getRentedProducts() throws RemoteException;

    /* ----Implemented for external users---- */

    /**
     * Adds a product to the user's cart.
     *
     * @param p The product to be added
     * @throws RemoteException
     */
    void addProductToCart(Product p) throws RemoteException;

    /**
     * Removes a product from the user's cart.
     *
     * @param p The product to be removed
     * @throws RemoteException
     */
    void removeProductFromCart(Product p) throws RemoteException;

    /**
     * Makes the user buy all the items in the cart from the catalogue.
     * It doesn't buy the products when a product can't be bought (ie. is missing from the catalogue).
     *
     * @param catalogue
     * @return whether he had enough money
     * @throws RemoteException
     */
    boolean buyProductsFromCatalogue(CatalogueManager catalogue) throws RemoteException;

    /**
     * @return The money this user holds, in pennys.
     * @throws RemoteException
     */
    int getMoneyLeft() throws RemoteException;

    /**
     * Adds the amount of money given into the wallet of this user.
     * @param amount In pennys
     * @return The new amount of money held by this user.
     * @throws RemoteException
     */
    int refillWallet(int amount) throws RemoteException;

    ArrayList<Product> getProductsInCart() throws RemoteException;
}
