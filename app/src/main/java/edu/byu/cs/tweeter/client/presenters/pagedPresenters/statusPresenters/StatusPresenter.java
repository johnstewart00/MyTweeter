package edu.byu.cs.tweeter.client.presenters.pagedPresenters.statusPresenters;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenters.pagedPresenters.PagedPresenter;
import edu.byu.cs.tweeter.client.services.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;

import java.util.Collections;
import java.util.List;

public abstract class StatusPresenter extends PagedPresenter<Status> {
    public StatusPresenter(PagedView view) {
        super(view);
    }

    public void getUserFromString(String clickable) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(), clickable, new userObserver());
    }
    public class statusObserver implements StatusService.StatusObserver {

        @Override
        public void logMessage(Exception ex) {

        }

        @Override
        public void displayMessage(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage(message);
        }

        @Override
        public void postingToastCancel() {

        }

        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage(ex.getMessage());
        }

        @Override
        public void loadItemsSuccess(List<Status> items, boolean hasMorePages) { // same as loadItemsSuccess
            isLoading = false;
            view.setLoadingFooter(false);
            lastItem = (items != null && items.size() > 0) ? items.get(items.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.loadMoreItems(items);
        }

        @Override
        public void displayMoreStory(boolean hasMorePages, List<Status> statuses) {
            isLoading = false;
            view.setLoadingFooter(false);
            lastItem = (statuses != null && statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.loadMoreItems(statuses);
        }
    }
}
