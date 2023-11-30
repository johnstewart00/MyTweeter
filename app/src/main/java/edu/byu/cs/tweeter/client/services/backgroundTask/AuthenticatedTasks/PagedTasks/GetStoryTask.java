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
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedTask<Status> {
    private Status lastStatus;
    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastItem,
                        Handler messageHandler) {
        super(messageHandler, authToken, targetUser, limit, lastItem);
        this.lastStatus = lastItem;
    }
    @Override
    protected Pair<List<Status>, Boolean> getItems( ) {
        StoryResponse response = null;
        ServerFacade serverFacade = new ServerFacade();
        StoryRequest storyRequest = new StoryRequest(authToken, targetUser.getAlias(), limit, lastStatus != null ? lastStatus.getPost() : null);
        try {
            System.out.println("In the getStory task, about to start the api call");
            response = serverFacade.getStory(storyRequest, "/story");
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