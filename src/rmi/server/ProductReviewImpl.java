package rmi.server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * Created by nakaze on 03/01/18.
 */
public class ProductReviewImpl extends UnicastRemoteObject implements Serializable, ProductReview {
    private final Date reviewDate;
    private final User author;
    private final int rating;
    private final String comment;

    ProductReviewImpl(Date reviewDate, User author, int rating, String comment) throws RemoteException {
        super();
        this.reviewDate = reviewDate;
        this.author = author;
        this.rating = rating;
        this.comment = comment;
    }

    @Override
    public Date getReviewDate() throws RemoteException {
        return reviewDate;
    }

    @Override
    public User getAuthor() throws RemoteException {
        return author;
    }

    @Override
    public int getRating() throws RemoteException {
        return rating;
    }

    @Override
    public String getComment() throws RemoteException {
        return comment;
    }
}
