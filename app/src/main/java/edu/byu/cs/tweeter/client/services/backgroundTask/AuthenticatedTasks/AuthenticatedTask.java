package edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks;

import android.os.Handler;

import edu.byu.cs.tweeter.client.services.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.services.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;

abstract public class AuthenticatedTask extends BackgroundTask {
    /**
     * Auth token for logged-in user.
     * This user is the "follower" in the relationship.
     */
    protected AuthToken authToken;

    public AuthenticatedTask(Handler messageHandler, AuthToken authToken) {
        super(messageHandler);
        this.authToken = authToken;
    }
}
