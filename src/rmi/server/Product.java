package rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

/**
 * A class representing a product.
 * <p>
 * A product has a name, an ID, a description, an owner and it is available for rent or not.
 * So we can rent this product, return it to a catalogue.
 * <p>
 * When we return the product, it is possible to add a review of this product.
 */
public interface Product extends Remote {
    String getProductName() throws RemoteException;

    int getProductId() throws RemoteException;

    Date getDateAdded() throws RemoteException;

    boolean isAvailable() throws RemoteException;

    User getOwner() throws RemoteException;

    String getProductDescription() throws RemoteException;

    void changeDescription(String newProductDescription) throws RemoteException;

    void setAvailability(boolean availability) throws RemoteException;

    /**
     * @return The list of reviews given for this product
     * @throws RemoteException
     */
    ArrayList<ProductReview> getReviews() throws RemoteException;

    ArrayList<User> getQueueingUsers() throws RemoteException;

    /**
     * When a product is unavailable when an user U wants to rent a product, he is added to its waiting list.
     *
     * @param u Unfortunate user
     * @throws RemoteException
     */
    void addUserToWaitingList(User u) throws RemoteException;

    /**
     * Make the necessary steps to return this product in the catalogue.<br>
     * When an user wants to return a product, it is made available, and if there are users in its waiting list, we choose
     * someone with the best priority, and if two users have the same priority, the one who rented less times gets the product.
     * If it's equal again, all comes to luck.
     *
     * @throws RemoteException
     */
    void returnProduct() throws RemoteException;

    /**
     * @return The total number of times this product has been rented.
     * @throws RemoteException
     */
    int getRentCount() throws RemoteException;

    /**
     * Sets this product as rented.
     *
     * @throws RemoteException
     */
    void isNowRented() throws RemoteException;

    /**
     * Adds a review for this product.
     *
     * @param user    The user who wrote this comment
     * @param rating  The rating given for this product
     * @param comment The comment given
     * @throws RemoteException
     */
    void addReview(User user, int rating, String comment) throws RemoteException;

    /**
     * A product is sellable when :
     * - it exists for at least two months
     * - it has already been rented.
     *
     * @return whether the product is sellable or not
     * @throws RemoteException
     */
    boolean isSellable() throws RemoteException;

    /**
     * @return The price of the product
     * @throws RemoteException
     */
    int getPrice() throws RemoteException;

    /**
     * @return The average rating of this product
     * @throws RemoteException
     */
    float averageRating() throws RemoteException;
}
