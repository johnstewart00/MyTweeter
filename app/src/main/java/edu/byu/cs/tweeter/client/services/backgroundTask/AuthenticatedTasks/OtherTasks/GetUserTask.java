package edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.OtherTasks;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.AuthenticatedTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.UserResponse;

/**
 * Background task that returns the profile for a specified user.
 */
public class GetUserTask extends AuthenticatedTask {
    private static final String LOG_TAG = "GetUserTask";
    public static final String USER_KEY = "user";
    private User currUser;
    private User user;
    /**
     * Alias (or handle) for user whose profile is being retrieved.
     */
    private String alias;
    public GetUserTask(AuthToken authToken, User currUser, String alias, Handler messageHandler) {
        super(messageHandler, authToken);
        this.alias = alias;
        this.currUser = currUser;
    }

    @Override
    protected void doTask( ) {
        user = getUser();
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, user);
    }

    private User getUser() {
//        User user = getFakeData().findUserByAlias(alias);
        UserResponse response = null;
        ServerFacade serverFacade = new ServerFacade();
        UserRequest request = new UserRequest(alias, currUser, authToken);
        try {
            System.out.println("Doing api call to GET USER with Alias: " + alias);
            response = serverFacade.getUser(request, "user");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TweeterRemoteException e) {
            throw new RuntimeException(e);
        }
        return response.getUser();
    }
}
