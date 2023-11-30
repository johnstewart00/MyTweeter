package edu.byu.cs.tweeter.client.services.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.services.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends BackgroundTask {
    private static final String LOG_TAG = "LoginTask";
    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    /**
     * The user's username (or "alias" or "handle"). E.g., "@susan".
     */
    private String username;
    /**
     * The user's password.
     */
    private String password;
    private User loggedInUser;
    private AuthToken authToken;

    public LoginTask(String username, String password, Handler messageHandler) {
        super(messageHandler);
        this.username = username;
        this.password = password;
    }

    @Override
    protected void doTask( ) {
        Pair<User, AuthToken> loginResult = doLogin();

        loggedInUser = loginResult.getFirst();
        authToken = loginResult.getSecond();
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, loggedInUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
    }


    private Pair<User, AuthToken> doLogin() {
//        User loggedInUser = getFakeData().getFirstUser();
//        AuthToken authToken = getFakeData().getAuthToken();
        LoginResponse response = null;
        ServerFacade serverFacade = new ServerFacade();
        LoginRequest loginRequest = new LoginRequest(username, password);
        try {
            System.out.println("In the login task, about to start the api call");
            response = serverFacade.login(loginRequest, "/login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TweeterRemoteException e) {
            throw new RuntimeException(e);
        }
        if(response == null){
            return null;
        }
        return new Pair<>(response.getUser(), response.getAuthToken());
    }
}
