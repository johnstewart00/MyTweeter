package edu.byu.cs.tweeter.client.presenters.pagedPresenters.userPresenters;

import android.widget.TextView;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenters.Presenter;
import edu.byu.cs.tweeter.client.presenters.pagedPresenters.PagedPresenter;
import edu.byu.cs.tweeter.client.services.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends UserPresenter {
    public FollowingPresenter(PagedView view) {
        super(view);
    }

    @Override
    public void getMoreitems(User user) {
        System.out.println("the view called for the following table");
        userService.getFollowing(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new userObserver());
    }
}
