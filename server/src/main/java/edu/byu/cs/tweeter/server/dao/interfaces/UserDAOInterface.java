package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.util.Pair;

public interface UserDAOInterface {
    Pair<User, AuthToken> login(LoginRequest request);

    Pair<User, AuthToken> register(String username, String password, String firstName, String lastName, String imageUrl);

    User getUser(String alias);

    LogoutResponse logout(LogoutRequest request);
}
