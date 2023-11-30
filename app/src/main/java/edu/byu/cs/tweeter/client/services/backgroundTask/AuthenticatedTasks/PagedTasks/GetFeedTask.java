package edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.PagedTasks;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.PagedTasks.PagedTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PagedTask<Status> {
    private Status lastStatus;
    public GetFeedTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                       Handler messageHandler) {
        super(messageHandler, authToken, targetUser, limit, lastStatus);
        this.lastStatus = lastStatus;
    }
    @Override
    protected Pair<List<Status>, Boolean> getItems( ) {
        FeedResponse response = null;
        ServerFacade serverFacade = new ServerFacade();
        FeedRequest feedRequest = new FeedRequest(authToken, targetUser.getAlias(), limit, lastStatus != null ? lastStatus.getPost() : null);
        try {
            System.out.println("In the getFeed task, about to start the api call");
            response = serverFacade.getFeed(feedRequest, "/feed");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TweeterRemoteException e) {
            throw new RuntimeException(e);
        }
        if(response == null){
            return null;
        }
        return new Pair<>(response.getStatuses(),response.getHasMorePages());
    }
}
