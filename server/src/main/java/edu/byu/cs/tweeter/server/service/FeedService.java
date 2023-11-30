package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.util.Pair;

public class FeedService {
    public FeedResponse getFeed(FeedRequest request) {
        if (request == null) throw new RuntimeException("[InternalError] sorry for the internal error");
        if(request.getTargetUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a targetUser alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        if(!getAuthDAO().isValidAuthToken(request.getTargetUserAlias(), request.getAuthToken())){
            throw new RuntimeException("[AuthError] you are not validated");
        }

        Pair<List<Status>, Boolean> pair = getFeedDAO().getStatuses(request.getTargetUserAlias(), request.getLimit(), request.getLastPost());
        return new FeedResponse(pair.getFirst(), pair.getSecond());
    }
    private AuthTokenDAO getAuthDAO(){
        return new AuthTokenDAO();
    }
    FeedDAO getFeedDAO () {
        return new FeedDAO();
    }
}
