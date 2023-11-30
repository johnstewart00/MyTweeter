package edu.byu.cs.tweeter.client.services.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.services.FollowService;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.OtherTasks.UnfollowTask;
import edu.byu.cs.tweeter.model.domain.User;

public class UnfollowHandler extends BaseHandler<FollowService.FollowObserver> {
    private User selectedUser;

    public UnfollowHandler(FollowService.FollowObserver observer, User selectedUser) {
        super(observer);
        this.selectedUser=selectedUser;
    }

    @Override
    public void handleSuccess(Message msg) {
        observer.unfollowSuccess();
    }

    @Override
    public void handleFail(String msg) {
        observer.displayMessage(msg);
    }
    @Override
    public String getTaskName(){
        return "unfollow";
    }

    @Override
    public void handleException(Exception ex) {
        observer.displayException(ex);
    }

    @Override
    public void doLeftOvers(Message msg) {
        observer.enableFollowButton(true);
    }
}
