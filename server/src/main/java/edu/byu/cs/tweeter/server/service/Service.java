package edu.byu.cs.tweeter.server.service;

import com.google.inject.Inject;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAOInterface;

public abstract class Service<T> {
    protected static final String NO_POSITIVE_LIMIT = "[Bad Request] Request must include a positive limit";
    protected static final String NO_TARGET_USER = "[Bad Request] Request needs to have a targetUser alias";
    protected static final String UNAUTHENTICATED = "[AuthError] you are not validated";
    protected static final String NO_FOLLOWER_ALIAS = "[Bad Request] Request must include a follower alias";
    protected static final String NO_FOLLOWEE_ALIAS = "[Bad Request] Request needs to have a followee alias";
    protected static final String NO_FOLLOWEE = "[Bad Request] you must provide a followee";
    protected static final String NO_FOLLOWER = "[Bad Request] you must provide a follower";
    protected static final String NO_CURRENT_USER = "[Bad Request] you have to provide a current user";
    protected static final String NO_POST = "[Bad Request] you must include a post";
    protected static final String NO_USERNAME = "[Bad Request] Missing a username";
    protected static final String NO_PASSWORD = "[Bad Request] Missing a password";
    protected static final String INVALID_CREDENTIALS = "[Bad Request] Invalid Credentials";

    protected AuthTokenDAOInterface authTokenDAO;

    @Inject
    public Service(AuthTokenDAOInterface authTokenDAO){
        setAuthTokenDAO(authTokenDAO);
    }


    protected void checkRequest( T request) {
        if (request == null) throw new RuntimeException("[InternalError] sorry for the internal error");
    }
    protected void checkNull (T object, String msg){
        if (object == null) throw new RuntimeException(msg);
    }
    protected void checkLimit(int limit){
        if(limit <= 0) {
            throw new RuntimeException(NO_POSITIVE_LIMIT);
        }
    }
    protected void checkAuthToken (String targetUserAlias, AuthToken authToken){
        if(!authTokenDAO.isValidAuthToken(targetUserAlias, authToken)) {
            throw new RuntimeException(UNAUTHENTICATED);
        }
    }

    public AuthTokenDAOInterface getAuthTokenDAO() {
        return authTokenDAO;
    }

    public void setAuthTokenDAO(AuthTokenDAOInterface authTokenDAO) {
        this.authTokenDAO = authTokenDAO;
    }
}
