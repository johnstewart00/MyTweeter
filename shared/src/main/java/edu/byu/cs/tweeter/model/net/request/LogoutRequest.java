package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LogoutRequest {
    private AuthToken authToken;
    private User currUser;
    public LogoutRequest(AuthToken authToken, User currUser){
        this.authToken = authToken;
        this.currUser = currUser;
    }
    private LogoutRequest(){};

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
    public User getCurrUser() {
        return currUser;
    }

    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }

}
