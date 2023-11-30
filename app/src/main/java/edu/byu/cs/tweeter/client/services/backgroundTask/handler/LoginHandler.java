package edu.byu.cs.tweeter.client.services.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.services.AuthenticateService;
import edu.byu.cs.tweeter.client.services.UserService;
import edu.byu.cs.tweeter.client.services.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for LoginTask
 */
public class LoginHandler extends BaseHandler<AuthenticateService.AuthObserver> {


    public LoginHandler(AuthenticateService.AuthObserver observer) {
        super(observer);
    }

    @Override
    public void handleSuccess(Message msg) {
        User loggedInUser=(User) msg.getData().getSerializable(LoginTask.USER_KEY);
        AuthToken authToken=(AuthToken) msg.getData().getSerializable(LoginTask.AUTH_TOKEN_KEY);
        observer.loginSuccess(loggedInUser, authToken);
    }

    @Override
    public void handleFail(String msg) {
        observer.displayMessage(msg);
    }
    @Override
    public String getTaskName(){
        return "login";
    }

    @Override
    public void handleException(Exception ex) {
        observer.displayException(ex);
    }

    @Override
    public void doLeftOvers(Message msg) {

    }
}
