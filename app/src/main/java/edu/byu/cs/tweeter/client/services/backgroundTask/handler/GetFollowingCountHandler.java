package edu.byu.cs.tweeter.client.services.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.services.FollowService;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.CountTasks.GetFollowingCountTask;

public class GetFollowingCountHandler extends BaseHandler<FollowService.FollowObserver> {


    public GetFollowingCountHandler(FollowService.FollowObserver observer) {
        super(observer);
    }

    @Override
    public void handleSuccess(Message msg) {
        int count=msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
        observer.setFolloweeCount(count);
    }

    @Override
    public void handleFail(String msg) {
        observer.displayMessage(msg);
    }
    @Override
    public String getTaskName(){
        return "get following count";
    }

    @Override
    public void handleException(Exception ex) {
        observer.displayException(ex);
    }

    @Override
    public void doLeftOvers(Message msg) {

    }
}
