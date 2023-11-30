package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;

public class IsFollowerResponse extends Response {
    private boolean isFollower;
    public IsFollowerResponse(boolean success, boolean isFollower){
        super(success);
        this.isFollower = isFollower;
    }
    public IsFollowerResponse(boolean success) {
        super(success);
    }

    public IsFollowerResponse(boolean success, String message) {
        super(success, message);
    }

    public boolean isFollower() {
        return isFollower;
    }

    public void setIsFollower(boolean isFollower) {
        this.isFollower = isFollower;
    }
}
