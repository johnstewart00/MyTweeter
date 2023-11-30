package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class PostStatusRequest {
    private Status post;
    private AuthToken auth;
    private User currUser;

    private PostStatusRequest(){};

    public PostStatusRequest(AuthToken auth, Status post, User currUser){
        this.post = post;
        this.auth = auth;
        this.currUser = currUser;
    }


    public Status getPost() {
        return post;
    }

    public void setPost(Status post) {
        this.post = post;
    }

    public AuthToken getAuth() {
        return auth;
    }

    public void setAuth (AuthToken auth) {
        this.auth = auth;
    }
    public User getCurrUser() {
        return currUser;
    }

    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }
}
