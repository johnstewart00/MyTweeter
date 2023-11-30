package edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.OtherTasks;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.AuthenticatedTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusReponse;

/**
 * Background task that posts a new status sent by a user.
 */
public class PostStatusTask extends AuthenticatedTask {
    private static final String LOG_TAG = "PostStatusTask";
    /**
     * The new status being sent. Contains all properties of the status,
     * including the identity of the user sending the status.
     */
    private Status status;
    private User currUser;

    public PostStatusTask(AuthToken authToken, Status status, User currUser, Handler messageHandler) {
        super(messageHandler, authToken);
        this.authToken = authToken;
        this.status = status;
        this.currUser = currUser;
    }

    @Override
    protected void doTask( ) {
        //TODO implement future functionality
        PostStatusReponse response = null;
        ServerFacade serverFacade = new ServerFacade();
        PostStatusRequest request = new PostStatusRequest(authToken, status, currUser);
        try {
            System.out.println("Doing api call to POST A STATUS");
            response = serverFacade.postStatus(request, "poststatus");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TweeterRemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {

    }
}
