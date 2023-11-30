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
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends CountTask {

    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(messageHandler, authToken, targetUser);
    }

    @Override
    protected void doTask( ) {
        //TODO something will be done here eventually
        System.out.println("this line should be run so that we are setting the Followers count");
        FollowersCountResponse response= null;
        ServerFacade serverFacade = new ServerFacade();
        FollowersCountRequest followersCountRequest = new FollowersCountRequest(targetUser, authToken);
        try {
            System.out.println("Getting the followers count from the API");
            response = serverFacade.getFollowersCount(followersCountRequest, "followerscount");
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
