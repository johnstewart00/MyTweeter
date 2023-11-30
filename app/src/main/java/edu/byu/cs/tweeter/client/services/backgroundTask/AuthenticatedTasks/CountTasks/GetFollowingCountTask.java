package edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.CountTasks;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.AuthenticatedTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends CountTask {
    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(messageHandler, authToken, targetUser);
    }

    @Override
    protected void doTask( ) {
        //TODO also probably should have something in the future
        System.out.println("this line should be run so that we are setting the Following count");
        FollowingCountResponse response= null;
        ServerFacade serverFacade = new ServerFacade();
        FollowingCountRequest followingCountRequest = new FollowingCountRequest(targetUser, authToken);
        try {
            System.out.println("Getting the following count from the API");
            response = serverFacade.getFollowingCount(followingCountRequest, "followingcount");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TweeterRemoteException e) {
            throw new RuntimeException(e);
        }
        setCount(response.getCount());
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putInt(COUNT_KEY, getCount());
    }
}
