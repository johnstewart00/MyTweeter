package edu.byu.cs.tweeter.client.services;

import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.OtherTasks.FollowTask;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.CountTasks.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.CountTasks.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.OtherTasks.IsFollowerTask;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.OtherTasks.UnfollowTask;
import edu.byu.cs.tweeter.client.services.backgroundTask.handler.*;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends Observer {

    public interface FollowObserver extends observer {
        void displayIsFollower(boolean isFollower);

        void enableFollowButton(boolean b);

        void setFollowerCount(int count);

        void setFolloweeCount(int count);

        void followSuccessful();

        void unfollowSuccess();
    }

    public void isFollower(AuthToken currUserAuthToken, User currUser, User selectedUser, FollowObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(currUserAuthToken,
                currUser, selectedUser, new IsFollowerHandler(observer));
        execute(isFollowerTask);
    }
    public void updateSelectedUserFollowingAndFollowers(AuthToken currUserAuthToken, User selectedUser, FollowObserver observer) {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(currUserAuthToken,
                selectedUser, new GetFollowersCountHandler(observer));
        execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(currUserAuthToken,
                selectedUser, new GetFollowingCountHandler(observer));
        execute(followingCountTask);
    }
    public void unfollow(AuthToken currUserAuthToken,User currentUser, User selectedUser, FollowObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(currUserAuthToken, currentUser,
                selectedUser, new UnfollowHandler(observer, selectedUser));
        execute(unfollowTask);
        observer.displayMessage("Removing " + selectedUser.getName() + "...");
    }
    public void follow(AuthToken currUserAuthToken,User currentUser, User selectedUser, FollowObserver observer) {
        FollowTask followTask = new FollowTask(currUserAuthToken, currentUser,
                selectedUser, new FollowHandler(observer, selectedUser));
        execute(followTask);
    }

}
