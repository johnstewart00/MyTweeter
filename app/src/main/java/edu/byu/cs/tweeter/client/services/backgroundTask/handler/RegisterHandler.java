package edu.byu.cs.tweeter.client.services.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import edu.byu.cs.tweeter.client.services.AuthenticateService;
import edu.byu.cs.tweeter.client.services.UserService;
import edu.byu.cs.tweeter.client.services.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterHandler extends BaseHandler<AuthenticateService.AuthObserver> {

    public RegisterHandler(AuthenticateService.AuthObserver observer) {
        super(observer);
    }

    @Override
    public void handleSuccess(Message msg) {
        User registeredUser=(User) msg.getData().getSerializable(RegisterTask.USER_KEY);
        AuthToken authToken=(AuthToken) msg.getData().getSerializable(RegisterTask.AUTH_TOKEN_KEY);
        observer.registerSuccess(registeredUser, authToken);
    }

    @Override
    public void handleFail(String msg) {
        observer.displayMessage(msg);
    }
    @Override
    public String getTaskName() {
        return "register";
    }

    @Override
    public void handleException(Exception ex) {
        observer.displayException(ex);
    }

    @Override
    public void doLeftOvers(Message msg) {

    }
}
