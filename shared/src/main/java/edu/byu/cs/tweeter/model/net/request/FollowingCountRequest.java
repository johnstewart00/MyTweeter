package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingCountRequest {
    private User follower;

    private AuthToken authToken;
    public FollowingCountRequest(){}
    public FollowingCountRequest(User follower, AuthToken authToken){
        this.follower = follower;
        this.authToken = authToken;
    }

    public User getFollower() {
        return follower;
    }
    public void setFollower(User follower){
        this.follower = follower;
    }
    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
