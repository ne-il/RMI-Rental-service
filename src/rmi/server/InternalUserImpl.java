package rmi.server;

import java.io.Serializable;
import java.io.UncheckedIOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by nakaze on 03/01/18.
 */
abstract class InternalUserImpl extends AbstractUser {
    private ArrayList<Product> rentedProducts = new ArrayList<>();
    private ArrayList<Product> queueingProducts = new ArrayList<>();
    private ArrayList<Product> addedProducts = new ArrayList<>(); // This one is created because the type check of equals doesn't work.
    private int rentCount = 0;

    InternalUserImpl(int userId, String firstName, String lastName) throws RemoteException {
        super(userId, firstName, lastName);
    }

    @Override
    public int compareTo(User u) throws RemoteException {
        try {
            if (this.getPriority() == u.getPriority())
                return this.rentCount - u.getRentCount();
            return -(this.getPriority() - u.getPriority());
        } catch (RemoteException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void addProductToCatalogue(CatalogueManager catalogueManager, String name, String description, int price) throws RemoteException {
        Product p = catalogueManager.addProduct(name, description, price, this);
        addedProducts.add(p);
    }

    @Override
    public void modifyProductDescription(Product p, String newDescription) throws RemoteException {
        if (this.equals(p.getOwner())) {
            p.changeDescription(newDescription);

        }
    }

    @Override
    public void rentProduct(Product p) throws RemoteException {

        if (p.isAvailable()) {
            rentCount++;
            rentedProducts.add(p);
            p.isNowRented();
        } else {
            if (!rentedProducts.contains(p)) {
                queueingProducts.add(p);
                p.addUserToWaitingList(this);
            }
        }
    }

    @Override
    public void unregisterFromProduct(Product p) throws RemoteException {
        queueingProducts.remove(p);
    }

    @Override
    public void removeProductFromCatalogue(CatalogueManager catalogueManager, Product p) throws RemoteException {
        if (equals(p.getOwner())) {
            // TODO p isn't a ProductImpl anymore for whatever reason, and so, it can't
            // TODO use the equals from ProductImpl and uses the one from Object instead...
            // TODO AND it is used that way in the ArrayList.remove() implementation. No luck there.
            if (!p.equals(addedProducts.get(0))) {
                System.err.println("Not equals to itself: " + p.getProductId() + " " + addedProducts.get(0).getProductId());
            }
            if (!addedProducts.remove(p))
                System.err.println("The product is not added by this user, even if he owns it. #Bullshit");
            catalogueManager.removeProduct(p);
        }
    }

    @Override
    public void returnProduct(Product p, int rating, String comment) throws RemoteException {
        if (rentedProducts.remove(p)) {
            p.addReview(this, rating, comment);
            p.returnProduct();
        }
    }

    @Override
    public ArrayList<Product> getAddedProducts() throws RemoteException {
        return addedProducts;
    }

    @Override
    public ArrayList<Product> getRentedProducts() throws RemoteException {
        return rentedProducts;
    }

    @Override
    public ArrayList<Product> getQueueingProducts() throws RemoteException {
        return queueingProducts;
    }

    @Override
    public int getRentCount() throws RemoteException {
        return rentCount;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            // TODO I removed the type test because it doesn't work on the client side.
            //    return obj instanceof InternalUserImpl && userId == ((User) obj).getUserId();
            return userId == ((User) obj).getUserId();
        } catch (RemoteException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public int getMoneyLeft() {
        return 0;
    }

    @Override
    public boolean buyProductsFromCatalogue(CatalogueManager catalogue) {
        return false;
    }

    @Override
    public void addProductToCart(Product p) {
        // Unimplemented
    }

    @Override
    public void removeProductFromCart(Product p) {
        // Unimplemented
    }

    @Override
    public int refillWallet(int amount) {
        return 0;
    }

    @Override
    public ArrayList<Product> getProductsInCart() {
        return new ArrayList<>();
    }
}
