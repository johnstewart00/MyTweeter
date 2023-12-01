package edu.byu.cs.tweeter.model.net.response;

public class IsFollowerResponse extends Response {
    private boolean follower;
    public IsFollowerResponse(boolean success, boolean follower){
        super(success);
        this.follower = follower;
    }
    public IsFollowerResponse(boolean success) {
        super(success);
    }

    public IsFollowerResponse(boolean success, String message) {
        super(success, message);
    }

    public boolean isFollower() {
        return follower;
    }

    public void setIsFollower(boolean isFollower) {
        this.follower = isFollower;
    }
}
