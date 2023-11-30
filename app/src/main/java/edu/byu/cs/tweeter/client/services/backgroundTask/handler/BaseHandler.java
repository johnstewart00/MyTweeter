package edu.byu.cs.tweeter.client.services.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.services.Observer;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.OtherTasks.FollowTask;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.PagedTasks.GetFeedTask;

public abstract class BaseHandler<T> extends Handler {
    protected T observer;
    public BaseHandler(T observer) {
        super(Looper.getMainLooper());
        this.observer = observer;
    }

    public abstract void handleSuccess(Message msg);
    public abstract String getTaskName();
    public abstract void handleFail(String msg);
    public abstract void handleException(Exception ex);
    public abstract void doLeftOvers(Message msg);

    @Override
    public void handleMessage(@NonNull Message msg) {
        System.out.println("Message returned was: " + msg.getData());
        boolean success=msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
        if(success) {
            handleSuccess(msg);
        }else if(msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
            String message=msg.getData().getString(FollowTask.MESSAGE_KEY);
            String task = getTaskName();
            handleFail("Failed to " + task + ": "+message);
        }else if(msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
            Exception ex=(Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
            handleException(ex);
        }
        doLeftOvers(msg);
    }
}
