package edu.byu.cs.tweeter.server.service;

import com.google.inject.Inject;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusReponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDAOInterface;

public class StatusService {
    private AuthTokenDAOInterface authTokenDAO;
    private StatusDAOInterface statusDAO;
    @Inject
    public StatusService(AuthTokenDAOInterface authTokenDAO, StatusDAOInterface statusDAO){
        setAuthTokenDAO(authTokenDAO);
        setStatusDAO(statusDAO);
    }

    public AuthTokenDAOInterface getAuthTokenDAO() {
        return authTokenDAO;
    }

    public void setAuthTokenDAO(AuthTokenDAOInterface authTokenDAO) {
        this.authTokenDAO = authTokenDAO;
    }

    public StatusDAOInterface getStatusDAO() {
        return statusDAO;
    }

    public void setStatusDAO(StatusDAOInterface statusDAO) {
        this.statusDAO = statusDAO;
    }

    public PostStatusReponse postStatus(PostStatusRequest request) {
        if (request == null) throw new RuntimeException("[InternalError] sorry for the internal error");
        if (request.getPost() == null) throw new RuntimeException("[Bad Request] you must include a post");
        if( request.getAuth() == null) throw new RuntimeException("[Bad Request] you must include a authToken");
        if (!authTokenDAO.isValidAuthToken(request.getCurrUser().getAlias(), request.getAuth()))
            throw new RuntimeException("[AuthError] unauthenticated request");
        return statusDAO.postStatus(request);
    }

}
