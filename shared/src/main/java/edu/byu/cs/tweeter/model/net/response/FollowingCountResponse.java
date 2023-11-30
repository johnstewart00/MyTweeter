package edu.byu.cs.tweeter.model.net.response;

public class FollowingCountResponse extends Response{
    int count;

    public FollowingCountResponse (int count){
        super(true);
        this.count = count;
    }
    public FollowingCountResponse(boolean success) {
        super(success);
    }

    public FollowingCountResponse(boolean success, String message) {
        super(success, message);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
