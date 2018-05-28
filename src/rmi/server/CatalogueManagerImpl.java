package rmi.server;

import java.io.Serializable;
import java.io.UncheckedIOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;

public class CatalogueManagerImpl extends UnicastRemoteObject implements Serializable, CatalogueManager {
    private ArrayList<Product> products = new ArrayList<>();
    private int productCount = 0;

    CatalogueManagerImpl() throws RemoteException {
        super();
    }

    @Override
    public Product addProduct(String name, String description, int price, User owner) throws RemoteException {
        ProductImpl p = new ProductImpl(name, productCount++, description, price, owner);
        products.add(p);
        return p;
    }

    @Override
    public void removeProduct(Product p) throws RemoteException {
        if (!products.remove(p))
            System.err.println("The product to be deleted doesn't exist.");
    }

    @Override
    public ArrayList<Product> getProducts() throws RemoteException {
        return products;
    }

    @Override
    public ArrayList<Product> getSellableProducts() throws RemoteException {
        return products.stream().filter(product -> {
            try {
                return product.isSellable();
            } catch (RemoteException e) {
                throw new UncheckedIOException(e);
            }
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Product getProductById(int id) throws RemoteException {
        return products.stream().filter(p -> {
            try {
                return p.getProductId() == id;
            } catch (RemoteException e) {
                throw new UncheckedIOException(e);
            }
        }).findAny().orElse(null);
    }
}
