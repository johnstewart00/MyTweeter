package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface AuthTokenDAOInterface {
    boolean isValidAuthToken(String targetUserAlias, AuthToken authToken);

    AuthToken setNewAuthToken(String username);

    boolean removeAuthToken(AuthToken authToken);
}
