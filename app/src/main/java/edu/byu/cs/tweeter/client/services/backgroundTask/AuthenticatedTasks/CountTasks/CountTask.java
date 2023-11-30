package edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.CountTasks;

import android.os.Handler;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.AuthenticatedTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class CountTask extends AuthenticatedTask {
    private static final String LOG_TAG = "GetCountTask";
    public static final String COUNT_KEY = "count";

    private Integer count = 20;
    /**
     * The user whose following count is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    protected User targetUser;
    public CountTask(Handler messageHandler, AuthToken authToken, User targetUser) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
    }

    public Integer getCount( ) {
        return count;
    }

    public void setCount(Integer count) {
        this.count=count;
    }
}
