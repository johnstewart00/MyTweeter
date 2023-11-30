package edu.byu.cs.tweeter.client.services.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.services.UserService;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.PagedTasks.GetFollowersTask;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

/**
 * Message handler (i.e., observer) for GetFollowersTask.
 */
public class GetFollowersHandler extends BaseHandler<UserService.UserObserver> {

    public GetFollowersHandler(UserService.UserObserver observer) {
        super(observer);
    }

    @Override
    public void handleSuccess(Message msg) {
        List<User> followers=(List<User>) msg.getData().getSerializable(GetFollowersTask.ITEMS_KEY);
        boolean hasMorePages=msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);
        observer.displayFollowers(followers, hasMorePages);
    }

    @Override
    public void handleFail(String msg) {
        observer.displayMessage(msg);
    }
    @Override
    public String getTaskName(){
        return "get followers";
    }

    @Override
    public void handleException(Exception ex) {
        observer.displayException(ex);
    }

    @Override
    public void doLeftOvers(Message msg) {

    }
}
