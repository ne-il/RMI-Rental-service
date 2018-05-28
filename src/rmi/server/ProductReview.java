package rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * A class representing a product review.
 * <p>
 * There is a date, an author, a rating and a comment.
 * Simple class.
 */
public interface ProductReview extends Remote {
    Date getReviewDate() throws RemoteException;

    User getAuthor() throws RemoteException;

    int getRating() throws RemoteException;

    String getComment() throws RemoteException;
}
