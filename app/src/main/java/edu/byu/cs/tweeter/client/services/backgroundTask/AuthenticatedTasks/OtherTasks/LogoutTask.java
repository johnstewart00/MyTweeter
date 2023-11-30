package edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.OtherTasks;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.AuthenticatedTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AuthenticatedTask {
    private static final String LOG_TAG = "LogoutTask";
    public User currUser;
    public LogoutTask(AuthToken authToken,User currUser, Handler messageHandler) {
        super(messageHandler, authToken);
        this.currUser = currUser;
    }

    @Override
    protected void doTask( ) {
        //TODO implement some future functionality
        LogoutResponse response = null;
        ServerFacade serverFacade = new ServerFacade();
        LogoutRequest request = new LogoutRequest(authToken, this.currUser);
        try {
            System.out.println("Doing api call to LOGOUT USER for " + this.currUser);
            response = serverFacade.logout(request, "logout");
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
