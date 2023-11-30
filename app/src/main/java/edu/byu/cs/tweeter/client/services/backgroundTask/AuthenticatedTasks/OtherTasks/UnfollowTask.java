package edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.OtherTasks;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.AuthenticatedTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.unFollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.unFollowResponse;

/**
 * Background task that removes a following relationship between two users.
 */
public class UnfollowTask extends AuthenticatedTask {
    private static final String LOG_TAG = "UnfollowTask";
    /**
     * The user that is being followed.
     */
    private User followee;
    private User currUser;

    public UnfollowTask(AuthToken authToken,User currUser, User followee, Handler messageHandler) {
        super(messageHandler, authToken);
        this.authToken = authToken;
        this.currUser = currUser;
        this.followee = followee;
    }

    @Override
    protected void doTask( ) {
        //TODO implement future functionality and logic
        // TODO do the thing for milestone 4
        unFollowResponse response = null;
        ServerFacade serverFacade = new ServerFacade();
        unFollowRequest request = new unFollowRequest(authToken, currUser, followee);
        try {
            System.out.println("Doing the unFollowTask, sending api call");
            response = serverFacade.unFollowTask(request, "unfollowtask");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TweeterRemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle ) {

    }
}
