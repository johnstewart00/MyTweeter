package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.User;

public class PostStatusReponse extends Response{
    public PostStatusReponse(boolean success) {
        super(success);
    }

    public PostStatusReponse(boolean success, String message) {
        super(success, message);
    }
}
