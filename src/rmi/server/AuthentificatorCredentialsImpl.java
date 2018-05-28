package rmi.server;

import java.io.Serializable;
import java.rmi.RemoteException;

public class AuthentificatorCredentialsImpl implements Serializable, AuthentificatorCredentials {
    private final String identifier;
    private final String password;

    public AuthentificatorCredentialsImpl(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    @Override
    public String getIdentifier() throws RemoteException {
        return identifier;
    }

    @Override
    public boolean checkLog(String identifier, String password) throws RemoteException {
        return identifier.equals(this.identifier) && password.equals(this.password);
    }
}
