package edu.byu.cs.tweeter.model.net.response;

public class unFollowResponse extends Response{
    public unFollowResponse(boolean success) {
        super(success);
    }

    public unFollowResponse(boolean success, String message) {
        super(success, message);
    }
}
