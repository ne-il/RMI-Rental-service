package rmi.server;

import java.io.Serializable;
import java.io.UncheckedIOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by nakaze on 03/01/18.
 */
public class ProductImpl extends UnicastRemoteObject implements Serializable, Product {
    private final String productName;
    private final int productId;
    private String productDescription;
    private final Date dateAdded;
    private boolean isAvailable;
    private final User owner;
    private final ArrayList<ProductReview> reviews = new ArrayList<>();
    private final ArrayList<User> queueingUsers = new ArrayList<>();
    private int rentCount;
    private int price;

    ProductImpl(String productName, int productId, String productDescription, int price, User owner) throws RemoteException {
        super();
        this.productName = productName;
        this.productId = productId;
        this.productDescription = productDescription;
        this.owner = owner;
        this.price = price;
        dateAdded = new Date();
        isAvailable = true;
    }

    @Override
    public String getProductName() {
        return productName;
    }

    @Override
    public int getProductId() {
        return productId;
    }

    @Override
    public Date getDateAdded() {
        return dateAdded;
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public User getOwner() {
        return owner;
    }

    @Override
    public String getProductDescription() {
        return productDescription;
    }

    @Override
    public void changeDescription(String newProductDescription) {
        this.productDescription = newProductDescription;
    }

    @Override
    public void setAvailability(boolean availability) {
        isAvailable = availability;
    }

    @Override
    public boolean equals(Object obj) {
//        return obj instanceof ProductImpl && productId == ((ProductImpl) obj).productId;
        try {
            System.err.println(">> Equals Product");
            boolean b = getProductId() == ((Product) obj).getProductId();
            if (!b) System.err.println(false + ": " + getProductId() + " is different from " + ((Product) obj).getProductId());
            return b;
        } catch (RemoteException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public ArrayList<ProductReview> getReviews() {
        return reviews;
    }

    @Override
    public ArrayList<User> getQueueingUsers() throws RemoteException {
        return queueingUsers;
    }

    @Override
    public void addUserToWaitingList(User u) throws RemoteException {
        queueingUsers.add(u);
    }

    /**
     * Gets the next user borrowing the product P following this rule : we choose
     * someone with the best priority, and if two users have the same priority, the one who rented less times gets the product.
     * If it's equal again, all comes to luck.
     *
     * @return The potential borrower
     */
    private Optional<User> getNextUserInWaitingList() {
        queueingUsers.sort((user, u) -> {
            try {
                return user.compareTo(u);
            } catch (RemoteException e) {
                throw new UncheckedIOException(e);
            }
        });
        return (!queueingUsers.isEmpty()) ? Optional.of(queueingUsers.get(0)) : Optional.empty();
    }

    @Override
    public void returnProduct() throws RemoteException {
        isAvailable = true;
        getNextUserInWaitingList().ifPresent(u -> {
            try {
                // TODO Notify this user
                u.unregisterFromProduct(this);
                u.rentProduct(this);
                queueingUsers.remove(u);
            } catch (RemoteException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    @Override
    public int getRentCount() throws RemoteException {
        return rentCount;
    }

    @Override
    public void isNowRented() throws RemoteException {
        isAvailable = false;
        rentCount++;
    }

    @Override
    public void addReview(User user, int rating, String comment) throws RemoteException {
        reviews.add(new ProductReviewImpl(new Date(), user, rating, comment));
    }

    @Override
    public boolean isSellable() throws RemoteException {
        return rentCount > 0 && dateAdded.toInstant().isBefore(new Date().toInstant().minus(2, ChronoUnit.MONTHS));
    }

    @Override
    public int getPrice() throws RemoteException {
        return price;
    }

    @Override
    public float averageRating() throws RemoteException {
        return ((float) reviews.stream().mapToInt(productReview -> {
            try {
                return productReview.getRating();
            } catch (RemoteException e) {
                throw new UncheckedIOException(e);
            }
        }).sum()) / ((float) reviews.size());
    }
}
