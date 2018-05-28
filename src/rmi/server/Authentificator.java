package rmi.server;

import com.sun.istack.internal.Nullable;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * A class that manages the log-ins.
 */
public interface Authentificator extends Remote {
    /**
     * Try to log in with an identifier and a password.
     *
     * @param identifier
     * @param password
     * @return An user if the couple (identifier, password) exists, or <b>null</b> otherwise.
     * @throws RemoteException
     */

    @Nullable
    User tryLog(String identifier, String password) throws RemoteException;
}
