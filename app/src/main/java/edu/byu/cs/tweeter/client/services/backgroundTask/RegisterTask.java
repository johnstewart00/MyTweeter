package edu.byu.cs.tweeter.client.services.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that creates a new user account and logs in the new user (i.e., starts a session).
 */
public class RegisterTask extends edu.byu.cs.tweeter.client.services.backgroundTask.BackgroundTask {
    private static final String LOG_TAG = "RegisterTask";
    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    /**
     * The user's first name.
     */
    private String firstName;
    /**
     * The user's last name.
     */
    private String lastName;
    /**
     * The user's username (or "alias" or "handle"). E.g., "@susan".
     */
    private String username;
    /**
     * The user's password.
     */
    private String password;
    /**
     * The base-64 encoded bytes of the user's profile image.
     */
    private String image;
    private User registeredUser;
    private AuthToken authToken;

    public RegisterTask(String firstName, String lastName, String username, String password,
                        String image, Handler messageHandler) {
        super(messageHandler);
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.image = image;
    }

    @Override
    protected void doTask( ) {
        Pair<User, AuthToken> registerResult = doRegister();

        registeredUser = registerResult.getFirst();
        authToken = registerResult.getSecond();
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, registeredUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
    }

    private Pair<User, AuthToken> doRegister() {
        RegisterResponse response = null;

        ServerFacade serverFacade = new ServerFacade();
        RegisterRequest registerRequest = new RegisterRequest(username, password, firstName, lastName, image);
        try {
            System.out.println("In the Register task, about to start the api call");
            response = serverFacade.register(registerRequest, "/register");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TweeterRemoteException e) {
            throw new RuntimeException(e);
        }
        if(response == null){
            return null;
        }
        System.out.println("Just returned from registering a new user, the username was: " + response.getRegisteredUser().getAlias());
        System.out.println("and the authtoken is " + response.getAuth());
        User registeredUser = response.getRegisteredUser();
        AuthToken authToken = response.getAuth();

        return new Pair<>(registeredUser, authToken);
    }
}
