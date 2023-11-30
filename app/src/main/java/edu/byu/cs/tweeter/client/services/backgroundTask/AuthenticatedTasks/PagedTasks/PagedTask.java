package edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.PagedTasks;

import android.os.Bundle;
import android.os.Handler;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.AuthenticatedTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

import java.io.Serializable;
import java.util.List;

public abstract class PagedTask<T> extends AuthenticatedTask {
    public static final String ITEMS_KEY= "items";
    public static final String MORE_PAGES_KEY = "more-pages";

    public User getTargetUser( ) {
        return targetUser;
    }

    protected User targetUser;
    /**
     * Maximum number of followers to return (i.e., page size).
     */
    protected int limit;
    /**
     * The last follower returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    protected T lastItem;
    protected List<T> items;

    protected boolean hasMorePages;

    public PagedTask(Handler messageHandler, AuthToken authToken, User targetUser, int limit, T lastFollower) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastFollower;
    }

    @Override
    protected void doTask( ) {
        Pair<List<T>, Boolean> pageOfStatus = getItems();
        items = pageOfStatus.getFirst();
        hasMorePages = pageOfStatus.getSecond();
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(ITEMS_KEY, (Serializable) items);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages);
    }
    protected abstract Pair<List<T>, Boolean> getItems();

    public int getLimit( ) {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit=limit;
    }

    public T getLastItem( ) {
        return lastItem;
    }

    public void setLastItem(T lastItem) {
        this.lastItem=lastItem;
    }

    public void setItems(List<T> items) {
        this.items=items;
    }

    public boolean isHasMorePages( ) {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages=hasMorePages;
    }
}
