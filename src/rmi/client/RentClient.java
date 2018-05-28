package rmi.client;

import rmi.server.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UncheckedIOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class RentClient {
    private static Registry registry;

    static {
        try {
            registry = LocateRegistry.getRegistry("192.168.1.100",1080);
            System.setProperty("java.rmi.server.codebase", "rmi:"
        } catch (RemoteException e) {
            throw new UncheckedIOException(e);
        }
    }

    private JTabbedPane tabbedPane1;
    private JPanel panel1;

    private JTable allProductTable;
    private JTable rentedProductsTable;
    private JTable addedProductsTable;


    private JButton rentButton;
    private JButton returnAProductButton;
    private JButton modifyProductDescriptionButton;
    private JButton addProductButton;
    private JButton displayReviewsButton;
    private JButton refreshButton;

    private User user;
    private CatalogueManager catalogueManager;
    private List<Product> allProducts;
    private List<Product> rentedProducts;
    private List<Product> addedProducts;

    String[] columnNames = {
            "Nom",
            "Description",
            "Disponible"
    };


    public RentClient(User user) throws RemoteException, NotBoundException {
        this.user = user;
        this.catalogueManager = (CatalogueManager) registry.lookup("catalogueManager");

        updateListModelAndTable();

        rentButton.addActionListener(e -> {
            try {
                int selectedRowIndex = allProductTable.getSelectedRow();
                if (selectedRowIndex != -1) {
                    Product selectedProduct = allProducts.get(selectedRowIndex);
                    JOptionPane.showMessageDialog(null, "Tu veux emprunter le produit suivant: \n" + selectedProduct.getProductName());

                    user.rentProduct(selectedProduct);
                    updateListModelAndTable();
                } else
                    JOptionPane.showMessageDialog(null, "Veuillez selectionner un produit");
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        });

        displayReviewsButton.addActionListener(e -> {
            try {
                int selectedRowIndex = allProductTable.getSelectedRow();
                if (selectedRowIndex != -1) {
                    Product selectedProduct = allProducts.get(selectedRowIndex);

                    List<ProductReview> reviews = selectedProduct.getReviews();
                    JPanel panel = new JPanel(new GridLayout(0, 1));

                    panel.add(new JLabel("Note MOYENNE: " + selectedProduct.averageRating()));
                    for (ProductReview review : reviews) {
                        panel.add(new JLabel(" "));
                        panel.add(new JLabel("Auteur: " + review.getAuthor().getFirstName() + " " + review.getAuthor().getLastName()));
                        panel.add(new JLabel("Note: " + review.getRating() + "/5"));
                        panel.add(new JLabel(review.getComment()));
                        panel.add(new JLabel(review.getReviewDate().toString()));
                    }
                    int result = JOptionPane.showConfirmDialog(null, panel, "RentClient", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez selectionner un produit");
                }
            } catch (RemoteException e1) {
                throw new UncheckedIOException(e1);
            }
        });

        returnAProductButton.addActionListener(e -> {
            try {
                int SelectedRowIndex = rentedProductsTable.getSelectedRow();
                if (SelectedRowIndex != -1) {
                    Product selectedProduct = rentedProducts.get(SelectedRowIndex);

                    if (selectedProduct.isAvailable()) {
                        JOptionPane.showMessageDialog(null, "Vous ne pouvez rendre un produit que vous n'avez pas empruntez");
                        return;
                    }

                    JOptionPane.showMessageDialog(null, "Vous voulez RENDRE le produit suivant: \n" + selectedProduct.getProductName());

                    JPanel panel = new JPanel(new GridLayout(0, 1));
                    panel.add(new JLabel("Veuillez laissez une evaluation de l'état du produit"));
                    panel.add(new JLabel(" Note sur 5 de l'etat du produit:"));
                    Integer[] items = {0, 1, 2, 3, 4, 5};
                    JComboBox<Integer> dropDownRating = new JComboBox<>(items);
                    panel.add(dropDownRating);
                    panel.add(new JLabel(" Commentaire:"));
                    JTextField inputDescription = new JTextField();
                    panel.add(inputDescription);
                    int result = JOptionPane.showConfirmDialog(null, panel, "RentClient", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        int rating = (Integer) dropDownRating.getSelectedItem();
                        String comment = inputDescription.getText();
                        JOptionPane.showMessageDialog(null, "Voila votre evaluation: \n" + rating + "/5 " + comment);

                        user.returnProduct(selectedProduct, rating, comment);
                        updateListModelAndTable();
                    } else {
                        System.out.println("Cancelled");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez selectionner un produit");
                }
            } catch (RemoteException e1) {
                throw new UncheckedIOException(e1);
            }
        });

        modifyProductDescriptionButton.addActionListener(e -> {
            try {
                int SelectedRowIndex = addedProductsTable.getSelectedRow();
                if (SelectedRowIndex != -1) {
                    Product selectedProduct = addedProducts.get(SelectedRowIndex);

                    JTextField inputField = new JTextField();
                    JPanel panel = new JPanel(new GridLayout(0, 1));
                    panel.add(new JLabel("Vous allez modifier la description du produit suivant: \n" + selectedProduct.getProductName()));

                    panel.add(new JLabel(" Entrez votre nouvelle description:"));
                    panel.add(inputField);

                    int result = JOptionPane.showConfirmDialog(null, panel, "RentClient", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        String newDescription = inputField.getText();
                        user.modifyProductDescription(selectedProduct, newDescription);
                        updateListModelAndTable();
                    } else {
                        System.out.println("Cancelled");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez selectionner un produit");
                }
            } catch (RemoteException e1) {
                throw new UncheckedIOException(e1);
            }
        });

        addProductButton.addActionListener(e -> {
            try {
                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Vous allez ajouter un nouveau produit au catalogue"));

                panel.add(new JLabel(" Nom du nouveau produit:"));
                JTextField inputName = new JTextField();
                panel.add(inputName);
                panel.add(new JLabel(" Description du nouveau produit:"));
                JTextField inputDescription = new JTextField();
                panel.add(inputDescription);

                int result = JOptionPane.showConfirmDialog(null, panel, "RentClient", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String newProductName = inputName.getText();
                    String newProductDescription = inputDescription.getText();

                    user.addProductToCatalogue(catalogueManager, newProductName, newProductDescription, 5);
                    updateListModelAndTable();
                } else {
                    System.out.println("Cancelled");
                }

            } catch (RemoteException e1) {
                throw new UncheckedIOException(e1);
            }

        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println("update Lists, Models, and Tables");
//                System.out.println("BEFORE THE UPDATE /n =============");
//
//                System.out.println("allProducts size" + allProducts.size());
//                System.out.println("rentedProducts size" + rentedProducts.size());
//                System.out.println("addedProducts size" + addedProducts.size());
                updateListModelAndTable();
//                System.out.println("======================/nAFTER THE UPDATE");
//
//                System.out.println("allProducts size" + allProducts.size());
//                System.out.println("rentedProducts size" + rentedProducts.size());
//                System.out.println("addedProducts size" + addedProducts.size());
            }
        });

        setupSelectionTable(allProductTable);
        setupSelectionTable(rentedProductsTable);
        setupSelectionTable(addedProductsTable);
    }

    public void updateListModelAndTable() {
        System.out.println("REDOWNLOAD  ALL LIST");

        try {
            allProducts = catalogueManager.getProducts();
            rentedProducts = user.getRentedProducts();
            addedProducts = user.getAddedProducts();

            fillTable(allProducts, allProductTable);
            fillTable(rentedProducts, rentedProductsTable);
            fillTable(addedProducts, addedProductsTable);

            AbstractTableModel m1 = (AbstractTableModel) allProductTable.getModel();
            m1.fireTableDataChanged();

            AbstractTableModel m2 = (AbstractTableModel) rentedProductsTable.getModel();
            m2.fireTableDataChanged();

            AbstractTableModel m3 = (AbstractTableModel) addedProductsTable.getModel();
            m3.fireTableDataChanged();
        } catch (RemoteException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void setupSelectionTable(JTable table) {
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void fillTable(List<Product> productsToDisplay, JTable tableToFill) {
        TableModel tableModel = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return productsToDisplay.size();
            }

            @Override
            public int getColumnCount() {
                return 3;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                Product p = productsToDisplay.get(rowIndex);
                try {
                    if (columnIndex == 0) {
                        return p.getProductName();
                    } else if (columnIndex == 1) {
                        return p.getProductDescription();
                    } else {
                        return p.isAvailable();
                    }
                } catch (RemoteException e) {
                    throw new UncheckedIOException(e);
                }

            }

            @Override
            public String getColumnName(int column) {
                return columnNames[column];
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) {
                    return Boolean.class;
                } else {
                    return Object.class;
                }
            }
        };

        tableToFill.setModel(tableModel);
    }

    private static User displayLoginPage() throws RemoteException, NotBoundException {
        JPanel panel = new JPanel(new GridLayout(0, 1));

        panel.add(new JLabel("Utilisateur"));
        JTextField inputUsername = new JTextField();
        panel.add(inputUsername);
        panel.add(new JLabel(" Mot de Passe:"));
        JTextField inputPassword = new JTextField();
        panel.add(inputPassword);

        int result = JOptionPane.showConfirmDialog(null, panel, "RentClient", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {

            Authentificator internalAuthentificator = (Authentificator) registry.lookup("internalAuthentificator");

            User user;
            user = internalAuthentificator.tryLog(inputUsername.getText(), inputPassword.getText());
            if (user == null) {
                JOptionPane.showMessageDialog(null, "Mauvais identifiants");
            }
            return user;
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        User user = null;
        do {
            user = displayLoginPage();
        } while (user == null);

        JFrame frame = new JFrame(user.getFirstName() + " " + user.getLastName());
        frame.setContentPane(new RentClient(user).panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
