package edu.byu.cs.tweeter.client.services;

import java.util.List;

import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.PagedTasks.GetFollowersTask;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.PagedTasks.GetFollowingTask;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.OtherTasks.GetUserTask;
import edu.byu.cs.tweeter.client.services.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.OtherTasks.LogoutTask;
import edu.byu.cs.tweeter.client.services.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.services.backgroundTask.handler.*;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService extends Observer {

    public interface UserObserver extends observer {

        void logoutSuccess();

        void getUserSuccess(User user);

        void displayFollowees(List<User> followees, boolean hasMorePages);

        void displayFollowers(List<User> followers, boolean hasMorePages);
    }
    public void logout(AuthToken currUserAuthToken, User currUser, UserObserver observer) {
        LogoutTask logoutTask = new LogoutTask(currUserAuthToken, currUser, new LogoutHandler(observer));
        execute(logoutTask);
    }
    public void getUser(AuthToken currUserAuthToken, User currUser, String userAlias, UserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken, currUser,
                userAlias, new GetUserHandler(observer));
        execute(getUserTask);
        observer.displayMessage("Getting user's profile...");
    }

    public void getFollowing(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, UserObserver observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new GetFollowingHandler(observer));
        execute(getFollowingTask);
    }

    public void loadFollowerItems(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, UserObserver observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new GetFollowersHandler(observer));
        execute(getFollowersTask);
    }
}
