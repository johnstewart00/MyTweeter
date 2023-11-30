package edu.byu.cs.tweeter.client.presenters.pagedPresenters.statusPresenters;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends StatusPresenter {
    public StoryPresenter(PagedView view){
        super(view);
    }

    @Override
    public void getMoreitems(User user) {
        statusService.getMoreStories(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new statusObserver());
    }

}
