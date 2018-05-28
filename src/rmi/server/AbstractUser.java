package rmi.server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public abstract class AbstractUser extends UnicastRemoteObject implements User, Serializable {
    final int userId;
    final String firstName;
    final String lastName;

    public AbstractUser(int userId, String firstName, String lastName) throws java.rmi.RemoteException {
        super();
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public int getUserId() throws RemoteException {
        return userId;
    }

    @Override
    public String getFirstName() throws RemoteException {
        return firstName;
    }

    @Override
    public String getLastName() throws RemoteException {
        return lastName;
    }
}
