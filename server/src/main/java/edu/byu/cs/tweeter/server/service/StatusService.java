package edu.byu.cs.tweeter.server.service;

import com.google.inject.Inject;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusReponse;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDAOInterface;

public class StatusService extends Service{
    private StatusDAOInterface statusDAO;
    @Inject
    public StatusService(StatusDAOInterface statusDAO, AuthTokenDAOInterface authTokenDAO){
        super(authTokenDAO);
        setStatusDAO(statusDAO);
    }

    public PostStatusReponse postStatus(PostStatusRequest request) {
        checkRequest(request);
        checkNull(request.getPost(),NO_POST);
        checkAuthToken(request.getCurrUser().getAlias(), request.getAuth());

        return statusDAO.postStatus(request);
    }

    public StatusDAOInterface getStatusDAO() {
        return statusDAO;
    }

    public void setStatusDAO(StatusDAOInterface statusDAO) {
        this.statusDAO = statusDAO;
    }

}
