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
import edu.byu.cs.tweeter.model.net.response.FollowResponse;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends AuthenticatedTask {
    private static final String LOG_TAG = "BackgroundTask";
    private User currentUser;
    /**
     * The user that is being followed.
     */
    private User followee;
    /**
     * Message handler that will receive task results.
     */

    public FollowTask(AuthToken authToken,User currUser, User followee, Handler messageHandler) {
        super(messageHandler, authToken);
        this.currentUser = currUser;
        this.followee = followee;
    }

    @Override
    protected void doTask( ) {
        // TODO do the thing for milestone 4
        FollowResponse response = null;
        ServerFacade serverFacade = new ServerFacade();
        FollowRequest request = new FollowRequest(currentUser, followee, authToken);
        try {
            System.out.println("Doing the FollowTask, sending api call");
            response = serverFacade.followTask(request, "followTask");
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
