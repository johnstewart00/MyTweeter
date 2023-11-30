package edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.PagedTasks;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.PagedTasks.PagedTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedTask<User> {
    User lastFollower;
    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(messageHandler, authToken, targetUser, limit, lastFollower);
        this.lastFollower = lastFollower;
    }
    @Override
    protected Pair<List<User>, Boolean> getItems( ) {
        FollowersResponse response = null;
        ServerFacade serverFacade = new ServerFacade();
        FollowersRequest followersRequest = new FollowersRequest(authToken, targetUser.getAlias(), limit, lastFollower != null ? lastFollower.getAlias() : null);
        try {
            System.out.println("In the getFollowers task, about to start the api call");
            response = serverFacade.getFollowers(followersRequest, "/getfollowers");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TweeterRemoteException e) {
            throw new RuntimeException(e);
        }
        if(response == null){
            return null;
        }
        List<User> following = response.getFollowers();
        return new Pair<> (following, response.getHasMorePages());
    }
}
