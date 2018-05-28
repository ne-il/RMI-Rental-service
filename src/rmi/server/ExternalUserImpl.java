package rmi.server;

import java.io.UncheckedIOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ExternalUserImpl extends AbstractUser {
    private final ArrayList<Product> productsInCart = new ArrayList<>();
    private int wallet;

    public ExternalUserImpl(int userId, String firstName, String lastName) throws RemoteException {
        super(userId, firstName, lastName);
    }

    @Override
    public int getPriority() throws RemoteException {
        return 0;
    }

    @Override
    public ArrayList<Product> getQueueingProducts() throws RemoteException {
        return new ArrayList<>();
    }

    @Override
    public int getRentCount() throws RemoteException {
        return 0;
    }

    @Override
    public int compareTo(User u) throws RemoteException {
        return 0;
    }

    @Override
    public void addProductToCatalogue(CatalogueManager catalogueManager, String name, String description, int price) throws RemoteException {
        // Unimplemented.
    }

    @Override
    public void modifyProductDescription(Product p, String newDescription) throws RemoteException {
        // Unimplemented
    }

    @Override
    public void rentProduct(Product p) throws RemoteException {
        // Unimplemented
    }

    @Override
    public void unregisterFromProduct(Product p) throws RemoteException {
        // Unimplemented
    }

    @Override
    public void removeProductFromCatalogue(CatalogueManager catalogueManager, Product p) throws RemoteException {
        // Unimplemented
    }

    @Override
    public void returnProduct(Product p, int rating, String comment) throws RemoteException {
        // Unimplemented
    }

    @Override
    public ArrayList<Product> getAddedProducts() throws RemoteException {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Product> getRentedProducts() throws RemoteException {
        return new ArrayList<>();
    }

    @Override
    public void addProductToCart(Product p) throws RemoteException {
        if (p.isSellable()) {
            productsInCart.add(p);
        }
    }

    @Override
    public void removeProductFromCart(Product p) throws RemoteException {
        if (!productsInCart.remove(p)) {
            System.err.println("The product isn't in the cart.");
        }
    }

    @Override
    public boolean buyProductsFromCatalogue(CatalogueManager catalogue) throws RemoteException {
        int cost = productsInCart.stream().mapToInt(p -> {
            try {
                return p.getPrice();
            } catch (RemoteException e) {
                throw new UncheckedIOException(e);
            }
        }).sum();
        if (wallet > cost) {
            // TODO Remove the products from the catalogue
            if (catalogue.getProducts().containsAll(productsInCart)) {
                wallet -= cost;
                productsInCart.forEach(p -> {
                    try {
                        catalogue.removeProduct(p);
                    } catch (RemoteException e) {
                        throw new UncheckedIOException(e);
                    }
                });
                productsInCart.clear();
                return true;
            } else {
                System.err.println("A product has disappeared into the void while you were wondering buying the products.");
                return false;
            }
        } else {
            System.err.println("You don't have enough wallet. Too bad your wallet doesn't refill itself.");
            return false;
        }
    }

    @Override
    public int getMoneyLeft() throws RemoteException {
        return wallet;
    }

    @Override
    public int refillWallet(int amount) throws RemoteException {
        wallet += amount;
        return wallet;
    }

    @Override
    public ArrayList<Product> getProductsInCart () throws RemoteException {
        return productsInCart;
    }
}
