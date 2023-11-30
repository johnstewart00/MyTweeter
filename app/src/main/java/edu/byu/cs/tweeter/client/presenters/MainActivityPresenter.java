package edu.byu.cs.tweeter.client.presenters;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.services.FollowService;
import edu.byu.cs.tweeter.client.services.StatusService;
import edu.byu.cs.tweeter.client.services.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainActivityPresenter extends Presenter {

    public interface View extends view {

        void displayIsFollower();
        void displayIsNotFollower();

        void setFollowButton(boolean value);

        void enableFollowButton(boolean value);

        void setFollowerCount(int count);

        void setFolloweeCount(int count);

        void cancelLogoutToast();

        void logoutUser();

        void postingToastCancel();
    }
    private static final String LOG_TAG = "MainActivity";
    private FollowService followService;
    private User selectedUser;
    private UserService userService;
    private StatusService statusService;
    private View view;
    public MainActivityPresenter(View view) {
        this.view= view;
        followService = new FollowService();
        userService = new UserService();
    }

    protected StatusService getStatusService() {
        if(statusService != null){
            return statusService;
        }
        return new StatusService();
    }

    public void FollowUnFollow(String followButtonText, String followingString, User selectedUser) {
        this.selectedUser = selectedUser;
        if (followButtonText.equals(followingString)) {
            followService.unfollow(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(),selectedUser, new followObserver());
        } else {
            followService.follow(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(), selectedUser, new followObserver());
            view.displayMessage("Adding " + selectedUser.getName() + "...");
        }
    }
    public void isFollower(User selectedUser) {
        followService.isFollower(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(), selectedUser, new followObserver());
    }
    public void updateSelectedUserFollowingAndFollowers(User selectedUser) {
        followService.updateSelectedUserFollowingAndFollowers(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new followObserver());
    }
    public void logout() {
        userService.logout(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(), new userObserver());
    }
    public void statusPosted(String post) {
        view.displayMessage("Posting Status...");
        List<String> urls = parseURLs(post);
        List<String> mentions = parseMentions(post);
        getStatusService().postStatus(post,Cache.getInstance().getCurrUser(),System.currentTimeMillis(), urls, mentions, new statusObserver());
    }
    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }
    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }
    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }
    private class statusObserver implements StatusService.StatusObserver {

        @Override
        public void logMessage(Exception ex) {
            // Log.e(LOG_TAG, ex.getMessage(), ex);
        }

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void postingToastCancel() {
            view.postingToastCancel();
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to post status because of exception: " + ex.getMessage());
        }

        @Override
        public void loadItemsSuccess(List<Status> statuses, boolean hasMorePages) {

        }

        @Override
        public void displayMoreStory(boolean hasMorePages, List<Status> statuses) {

        }
    }
    private class userObserver implements UserService.UserObserver {

        @Override
        public void logoutSuccess() {
            view.cancelLogoutToast();
            view.logoutUser();
        }
        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to logout because of exception: " + ex.getMessage());
        }

        @Override
        public void getUserSuccess(User user) {

        }

        @Override
        public void displayFollowees(List<User> followees, boolean hasMorePages) {

        }

        @Override
        public void displayFollowers(List<User> followers, boolean hasMorePages) {

        }
    }

    private class followObserver implements FollowService.FollowObserver {

        @Override
        public void displayIsFollower(boolean isFollower) {
            // If logged in user if a follower of the selected user, display the follow button as "following"
            if (isFollower) {
                view.displayIsFollower();
            } else {
                view.displayIsNotFollower();
            }

        }

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("Failed to determine following relationship because of exception: " + ex.getMessage());
        }

        @Override
        public void enableFollowButton(boolean value) {
            view.enableFollowButton(value);
        }

        @Override
        public void setFollowerCount(int count) {
            view.setFollowerCount(count);
        }

        @Override
        public void setFolloweeCount(int count) {
            view.setFolloweeCount(count);
        }

        @Override
        public void followSuccessful() {
            followService.updateSelectedUserFollowingAndFollowers(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new followObserver());
            view.setFollowButton(false);
        }

        @Override
        public void unfollowSuccess() {
            followService.updateSelectedUserFollowingAndFollowers(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new followObserver());
            view.setFollowButton(true);
        }
    }
}
