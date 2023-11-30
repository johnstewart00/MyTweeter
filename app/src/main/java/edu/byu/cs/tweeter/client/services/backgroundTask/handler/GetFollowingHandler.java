package edu.byu.cs.tweeter.client.services.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.services.UserService;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.PagedTasks.GetFollowingTask;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

/**
 * Message handler (i.e., observer) for GetFollowingTask.
 */
public class GetFollowingHandler extends BaseHandler<UserService.UserObserver> {

    public GetFollowingHandler(UserService.UserObserver observer) {
        super(observer);
    }

    @Override
    public void handleSuccess(Message msg) {
        List<User> followees=(List<User>) msg.getData().getSerializable(GetFollowingTask.ITEMS_KEY);
        boolean hasMorePages=msg.getData().getBoolean(GetFollowingTask.MORE_PAGES_KEY);
        observer.displayFollowees(followees, hasMorePages);
    }

    @Override
    public void handleFail(String msg) {
        observer.displayMessage(msg);
    }
    @Override
    public String getTaskName(){
        return "get following";
    }

    @Override
    public void handleException(Exception ex) {
        observer.displayException(ex);
    }

    @Override
    public void doLeftOvers(Message msg) {

    }
}
