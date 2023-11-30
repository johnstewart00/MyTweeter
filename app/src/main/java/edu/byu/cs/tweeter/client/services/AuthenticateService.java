package edu.byu.cs.tweeter.client.services;

import edu.byu.cs.tweeter.client.services.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.services.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.services.backgroundTask.handler.LoginHandler;
import edu.byu.cs.tweeter.client.services.backgroundTask.handler.RegisterHandler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticateService extends Observer {
    public interface AuthObserver extends observer {
        void loginSuccess(User loggedInUser, AuthToken authToken); // combine with register success

        void registerSuccess(User registeredUser, AuthToken authToken);
    }
    public void login(String alias, String password, AuthObserver observer) {
        LoginTask loginTask = new LoginTask(alias, password, new LoginHandler(observer));
        execute(loginTask);
    }

    public void register(String firstName, String lastName, String alias, String password, String imageBytesBase64, AuthObserver observer) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName, alias, password, imageBytesBase64, new RegisterHandler(observer));
        execute(registerTask);
    }
}
