package edu.byu.cs.tweeter.client.presenters.pagedPresenters.userPresenters;

import edu.byu.cs.tweeter.client.presenters.pagedPresenters.PagedPresenter;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class UserPresenter extends PagedPresenter<User> {
    public UserPresenter(PagedView view) {
        super(view);
    }
}
