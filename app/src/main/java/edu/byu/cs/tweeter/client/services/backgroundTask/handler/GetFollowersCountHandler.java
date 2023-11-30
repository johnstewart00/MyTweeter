package edu.byu.cs.tweeter.client.services.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.services.FollowService;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.CountTasks.GetFollowersCountTask;

public class GetFollowersCountHandler extends BaseHandler<FollowService.FollowObserver> {

    public GetFollowersCountHandler(FollowService.FollowObserver observer) {
        super(observer);
    }

    @Override
    public void handleSuccess(Message msg) {
        int count=msg.getData().getInt(GetFollowersCountTask.COUNT_KEY);
        observer.setFollowerCount(count);
    }
    @Override
    public String getTaskName(){
        return "get followers count";
    }
    @Override
    public void handleFail(String msg) {
        observer.displayMessage(msg);
    }

    @Override
    public void handleException(Exception ex) {
        observer.displayException(ex);
    }

    @Override
    public void doLeftOvers(Message msg) {

    }
}
