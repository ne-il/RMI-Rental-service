package rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * A class representing a couple of identifier and password.
 */
public interface AuthentificatorCredentials extends Remote {
    /**
     * @return The identifier of this instance
     * @throws RemoteException
     */
    String getIdentifier() throws RemoteException;

    /**
     * Checks if the identifier and the password are the ones in this instance
     *
     * @param identifier
     * @param password
     * @return Whether it matches or not
     * @throws RemoteException
     */
    boolean checkLog(String identifier, String password) throws RemoteException;
}
