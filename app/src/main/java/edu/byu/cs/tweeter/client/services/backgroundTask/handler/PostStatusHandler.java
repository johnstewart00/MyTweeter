package edu.byu.cs.tweeter.client.services.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.services.StatusService;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.OtherTasks.PostStatusTask;

// PostStatusHandler
public class PostStatusHandler extends BaseHandler<StatusService.StatusObserver> {

    public PostStatusHandler(StatusService.StatusObserver observer) {
        super(observer);
    }

    @Override
    public void handleSuccess(Message msg) {
        observer.postingToastCancel();
        observer.displayMessage("Successfully Posted!");
    }

    @Override
    public void handleFail(String msg) {
        observer.displayMessage(msg);
    }
    @Override
    public String getTaskName(){
        return "post status";
    }

    @Override
    public void handleException(Exception ex) {
        observer.displayException(ex);
    }

    @Override
    public void doLeftOvers(Message msg) {

    }
}
