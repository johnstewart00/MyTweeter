package edu.byu.cs.tweeter.client.presenters.pagedPresenters.statusPresenters;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenters.pagedPresenters.PagedPresenter;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends StatusPresenter {
    public FeedPresenter(PagedPresenter.PagedView view){
        super(view);
    }

    @Override
    public void getMoreitems(User user) {
        statusService.loadMoreItems(Cache.getInstance().getCurrUserAuthToken(),user, PAGE_SIZE, lastItem, new FeedPresenter.statusObserver());
    }
}
