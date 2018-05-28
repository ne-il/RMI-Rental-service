package rmi.server;

import java.rmi.RemoteException;

public class Teacher extends InternalUserImpl {
    Teacher(int userId, String firstName, String lastName) throws RemoteException {
        super(userId, firstName, lastName);
    }

    @Override
    public int getPriority() throws RemoteException {
        return 2;
    }

    @Override
    public boolean equals(Object obj) {
//        return super.equals(obj) && obj instanceof Teacher;
        return super.equals(obj);
    }
}
