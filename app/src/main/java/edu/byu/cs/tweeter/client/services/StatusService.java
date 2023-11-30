package edu.byu.cs.tweeter.client.services;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.PagedTasks.GetFeedTask;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.PagedTasks.GetStoryTask;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.OtherTasks.PostStatusTask;
import edu.byu.cs.tweeter.client.services.backgroundTask.handler.GetFeedHandler;
import edu.byu.cs.tweeter.client.services.backgroundTask.handler.GetStoryHandler;
import edu.byu.cs.tweeter.client.services.backgroundTask.handler.PostStatusHandler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService extends Observer{

    public interface StatusObserver extends observer {

        void logMessage(Exception ex);

        void postingToastCancel(); // make a new interface

        void loadItemsSuccess(List<Status> statuses, boolean hasMorePages); // combine

        void displayMoreStory(boolean hasMorePages, List<Status> statuses);
    }
    public void getMoreStories(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, StatusObserver observer) {
        System.out.println("getting more stories");
        GetStoryTask getStoryTask = new GetStoryTask(currUserAuthToken,
                user, pageSize, lastStatus, new GetStoryHandler(observer));
        execute(getStoryTask);
    }

    public void loadMoreItems(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, StatusObserver observer) {
        System.out.println("getting more feed");
        GetFeedTask getFeedTask = new GetFeedTask(currUserAuthToken,
                user, pageSize, lastStatus, new GetFeedHandler(observer));
        execute(getFeedTask);
    }

    public void postStatus(String post, User currUser, long currentTimeMillis, List<String> urls, List<String> mentions, StatusObserver observer) {
        System.out.println("trying to print a status");
        try {
            Status newStatus = new Status(post, currUser, currentTimeMillis, urls, mentions);
            PostStatusTask statusTask = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(),
                    newStatus,currUser, new PostStatusHandler(observer));
            execute(statusTask);
        } catch (Exception ex) {
            observer.logMessage(ex);
            observer.displayMessage("Failed to post the status because of exception: " + ex.getMessage());
        }
    }

}
