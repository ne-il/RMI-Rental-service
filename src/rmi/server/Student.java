package rmi.server;

import java.rmi.RemoteException;

public class Student extends InternalUserImpl {
    Student(int userId, String firstName, String lastName) throws RemoteException {
        super(userId, firstName, lastName);
    }

    @Override
    public int getPriority() throws RemoteException {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {

//        return super.equals(obj) && obj instanceof Student;
        return super.equals(obj);
    }
}
