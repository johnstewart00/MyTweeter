package edu.byu.cs.tweeter.client.services.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.services.UserService;
import edu.byu.cs.tweeter.client.services.backgroundTask.AuthenticatedTasks.OtherTasks.LogoutTask;

// LogoutHandler
public class LogoutHandler extends BaseHandler<UserService.UserObserver> {

    public LogoutHandler(UserService.UserObserver observer) {
        super(observer);
    }

    @Override
    public void handleSuccess(Message msg) {
        observer.logoutSuccess();
    }

    @Override
    public void handleFail(String msg) {
        observer.displayMessage(msg);
    }
    @Override
    public String getTaskName(){
        return "logout";
    }

    @Override
    public void handleException(Exception ex) {
        observer.displayException(ex);
    }

    @Override
    public void doLeftOvers(Message msg) {

    }
}
