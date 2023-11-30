package edu.byu.cs.tweeter.client.presenters.pagedPresenters;

import android.widget.TextView;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenters.Presenter;
import edu.byu.cs.tweeter.client.services.StatusService;
import edu.byu.cs.tweeter.client.services.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public abstract class PagedPresenter<T> extends Presenter {

    public interface PagedView<T> extends view {
        void gotUserSuccess(User user);
        void setLoadingFooter(boolean value);

        void loadMoreItems(List<T> items);
    }
    protected T lastItem;

    protected boolean hasMorePages;

    protected boolean isLoading = false;
    protected static final int PAGE_SIZE = 10;
    protected UserService userService;
    protected StatusService statusService;
    protected PagedView view;
    public PagedPresenter(PagedView view) {
        this.view = view;
        userService = new UserService();
        statusService = new StatusService();
    }
    public void getUser(TextView userAlias) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(),Cache.getInstance().getCurrUser(), userAlias.getText().toString(), new userObserver());
    }
    public abstract void getMoreitems(User user);
    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            getMoreitems(user);
        }
    }
    public class userObserver implements UserService.UserObserver {

            @Override
        public void logoutSuccess() {

        }

        @Override
        public void displayMessage(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage(message);
        }

        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage(ex.getMessage());
        }

        @Override
        public void getUserSuccess(User user) {
            view.gotUserSuccess(user);
        }

        @Override
        public void displayFollowees(List<User> followees, boolean hasMorePages) { // same as displayFollowers
            isLoading = false;
            view.setLoadingFooter(false);
            lastItem = (followees != null && followees.size() > 0) ? (T) followees.get(followees.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.loadMoreItems(followees);
        }

        @Override
        public void displayFollowers(List<User> followers, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastItem = (followers.size() > 0) ? (T) followers.get(followers.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.loadMoreItems(followers);
        }
    }
    public boolean getHasMorePages( ) {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages=hasMorePages;
    }
    public boolean isLoading( ) {
        return isLoading;
    }
}
