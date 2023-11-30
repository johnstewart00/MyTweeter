package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterResponse extends Response{
    private User registeredUser;
    private AuthToken auth;

    public RegisterResponse(String message){
        super(false, message);
    }

    public RegisterResponse(boolean success) {
        super(success);
    }

    public RegisterResponse(User user, AuthToken authToken) {
        super(true, null);
        this.registeredUser = user;
        this.auth = authToken;
    }

    public User getRegisteredUser() {
        return registeredUser;
    }

    public void setRegisteredUser(User registeredUser) {
        this.registeredUser = registeredUser;
    }
    public AuthToken getAuth() {
        return auth;
    }

    public void setAuth(AuthToken auth) {
        this.auth = auth;
    }
}
