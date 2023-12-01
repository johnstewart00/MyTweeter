package edu.byu.cs.tweeter.server.service;

import com.google.inject.Inject;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.FeedDAOInterface;
import edu.byu.cs.tweeter.util.Pair;

public class FeedService {

    private FeedDAOInterface feedDAO;
    private AuthTokenDAOInterface authTokenDAO;

    @Inject
    public FeedService(FeedDAOInterface feedDAO, AuthTokenDAOInterface authTokenDAO){
        setFeedDAO(feedDAO);
        setAuthTokenDAO(authTokenDAO);
    }

    public FeedDAOInterface getFeedDAO() {
        return feedDAO;
    }

    public void setFeedDAO(FeedDAOInterface feedDAO) {
        this.feedDAO = feedDAO;
    }

    public AuthTokenDAOInterface getAuthTokenDAO() {
        return authTokenDAO;
    }

    public void setAuthTokenDAO(AuthTokenDAOInterface authTokenDAO) {
        this.authTokenDAO = authTokenDAO;
    }

    public FeedResponse getFeed(FeedRequest request) {
        if (request == null) throw new RuntimeException("[InternalError] sorry for the internal error");
        if(request.getTargetUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a targetUser alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        if(!authTokenDAO.isValidAuthToken(request.getTargetUserAlias(), request.getAuthToken())){
            throw new RuntimeException("[AuthError] you are not validated");
        }

        Pair<List<Status>, Boolean> pair = feedDAO.getStatuses(request.getTargetUserAlias(), request.getLimit(), request.getLastPost());
        return new FeedResponse(pair.getFirst(), pair.getSecond());
    }

}
