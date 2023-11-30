package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusReponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.StatusDAO;

public class StatusService {
    public PostStatusReponse postStatus(PostStatusRequest request) {
        if (request == null) throw new RuntimeException("[InternalError] sorry for the internal error");
        if (request.getPost() == null) throw new RuntimeException("[Bad Request] you must include a post");
        if( request.getAuth() == null) throw new RuntimeException("[Bad Request] you must include a authToken");
        if (!getAuthTokenDAO().isValidAuthToken(request.getCurrUser().getAlias(), request.getAuth()))
            throw new RuntimeException("[AuthError] unauthenticated request");
        StatusDAO statusDAO = new StatusDAO();
        return statusDAO.postStatus(request);
    }

    private AuthTokenDAO getAuthTokenDAO() {
        return new AuthTokenDAO();
    }
}
