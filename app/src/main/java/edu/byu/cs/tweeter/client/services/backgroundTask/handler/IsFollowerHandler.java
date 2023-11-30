package edu.byu.cs.tweeter.client.services.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.services.FollowService;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.OtherTasks.IsFollowerTask;

public class IsFollowerHandler extends BaseHandler<FollowService.FollowObserver> {


    public IsFollowerHandler(FollowService.FollowObserver observer) {
        super(observer);
    }

    @Override
    public void handleSuccess(Message msg) {
        boolean isFollower=msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.displayIsFollower(isFollower);
    }

    @Override
    public void handleFail(String msg) {
        observer.displayMessage(msg);
    }
    @Override
    public String getTaskName(){
        return "determine following relationship";
    }

    @Override
    public void handleException(Exception ex) {
        observer.displayException(ex);
    }

    @Override
    public void doLeftOvers(Message msg) {

    }
}
