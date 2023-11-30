package edu.byu.cs.tweeter.model.net.response;

public class FollowersCountResponse extends Response {

    int count;

    public FollowersCountResponse (int count){
        super(true);
        this.count = count;
    }
    public FollowersCountResponse(boolean success) {
        super(success);
    }

    public FollowersCountResponse(boolean success, String message) {
        super(success, message);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
