package edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.PagedTasks;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.AuthenticatedTask;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.PagedTasks.PagedTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedTask<User> {
    User lastFollowee;
    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(messageHandler, authToken, targetUser, limit, lastFollowee);
        this.lastFollowee = lastFollowee;
    }
    @Override
    protected Pair<List<User>, Boolean> getItems( ) {

        FollowingResponse response = null;
        ServerFacade serverFacade = new ServerFacade();
        FollowingRequest followingRequest = new FollowingRequest(authToken, targetUser.getAlias(), limit, lastFollowee != null ? lastFollowee.getAlias() : null);
        try {
            System.out.println("In the getFollowing task, about to start the api call");
            response = serverFacade.getFollowees(followingRequest, "/getfollowing");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TweeterRemoteException e) {
            throw new RuntimeException(e);
        }
        if(response == null){
            return null;
        }
        List<User> following = response.getFollowees();
        return new Pair<> (following, response.getHasMorePages());
    }
}
