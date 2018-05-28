package rmi.server;

import java.io.Serializable;
import java.io.UncheckedIOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Optional;

public class AuthentificatorImpl extends UnicastRemoteObject implements Authentificator, Serializable {
    private final HashMap<AuthentificatorCredentials, User> mapper = new HashMap<>();
    private int userCount;

    AuthentificatorImpl() throws RemoteException {
        super();
    }

    /**
     * Only used server side.
     * Create an user with a student priority.
     *
     * @param firstName
     * @param lastName
     * @param identifiant
     * @param password
     * @return whether the user has been successfully created or not.<br>
     *     It is not when the identifier already exists.
     * @throws RemoteException
     */
    public boolean createStudent(String firstName, String lastName, String identifiant, String password) throws RemoteException {
        User newUser = new Student(userCount++, firstName, lastName);
        return checkUnique(identifiant, password, newUser);
    }

    private boolean checkUnique(String identifier, String password, User newUser) {
        AuthentificatorCredentials ac = new AuthentificatorCredentialsImpl(identifier, password);
        if (mapper.keySet().stream().map(x -> {
            try {
                return x.getIdentifier();
            } catch (RemoteException e) {
                throw new UncheckedIOException(e);
            }
        }).noneMatch(i -> i.equals(identifier))) {
            mapper.put(ac, newUser);
            return true;
        }
        return false;
    }

    /**
     * Only used server side.
     * Creates an user with a teacher priority.
     *
     * @param firstName
     * @param lastName
     * @param identifiant
     * @param password
     * @return whether the user has been successfully created or not.<br>
     *     It is not when the identifier already exists.
     * @throws RemoteException
     */
    public boolean createTeacher(String firstName, String lastName, String identifiant, String password) throws RemoteException {
        User newUser = new Teacher(userCount++, firstName, lastName);
        return checkUnique(identifiant, password, newUser);
    }

    public boolean createNormalUser (String firstName, String lastName, String identifiant, String password) throws RemoteException {
        User newUser = new ExternalUserImpl(userCount++, firstName, lastName);
        return checkUnique(identifiant, password, newUser);
    }

    @Override
    public User tryLog(String identifier, String password) throws RemoteException {
        Optional<AuthentificatorCredentials> o = mapper.keySet().stream().filter(ac -> {
            try {
                return ac.checkLog(identifier, password);
            } catch (RemoteException e) {
                throw new UncheckedIOException(e);
            }
        }).findAny();

        if (o.isPresent())
            return mapper.get(o.get());
        return null;
    }

    public HashMap<AuthentificatorCredentials, User> getMapper() {
        return mapper;
    }
}
