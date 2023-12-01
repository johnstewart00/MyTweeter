package edu.byu.cs.tweeter.server.service;

import com.google.inject.Inject;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.FeedDAOInterface;
import edu.byu.cs.tweeter.util.Pair;

public class FeedService extends Service{

    private FeedDAOInterface feedDAO;

    @Inject
    public FeedService(FeedDAOInterface feedDAO, AuthTokenDAOInterface authTokenDAO){
        super(authTokenDAO);
        setFeedDAO(feedDAO);
    }

    public FeedResponse getFeed(FeedRequest request) {
        checkRequest(request);
        checkNull(request.getTargetUserAlias(), NO_TARGET_USER);
        checkLimit(request.getLimit());
        checkAuthToken(request.getTargetUserAlias(), request.getAuthToken());

        Pair<List<Status>, Boolean> pair = feedDAO.getStatuses(request.getTargetUserAlias(), request.getLimit(), request.getLastPost());
        return new FeedResponse(pair.getFirst(), pair.getSecond());
    }

    public FeedDAOInterface getFeedDAO() {
        return feedDAO;
    }

    public void setFeedDAO(FeedDAOInterface feedDAO) {
        this.feedDAO = feedDAO;
    }

}
