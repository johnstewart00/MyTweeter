package edu.byu.cs.tweeter.server.service;

import com.google.inject.Inject;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.StoryDAOInterface;
import edu.byu.cs.tweeter.util.Pair;

public class StoryService {
    private StoryDAOInterface storyDAO;
    private AuthTokenDAOInterface authTokenDAO;

    @Inject
    public StoryService(StoryDAOInterface storyDAO, AuthTokenDAOInterface authTokenDAO){
        setStoryDAO(storyDAO);
        setAuthTokenDAO(authTokenDAO);
    }

    public StoryDAOInterface getStoryDAO() {
        return storyDAO;
    }

    public void setStoryDAO(StoryDAOInterface storyDAO) {
        this.storyDAO = storyDAO;
    }

    public AuthTokenDAOInterface getAuthTokenDAO() {
        return authTokenDAO;
    }

    public void setAuthTokenDAO(AuthTokenDAOInterface authTokenDAO) {
        this.authTokenDAO = authTokenDAO;
    }

    public StoryResponse getStory(StoryRequest request) {
        if (request == null) throw new RuntimeException("[InternalError] sorry for the internal error");
        if(request.getTargetUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a targetUser alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        if (!authTokenDAO.isValidAuthToken(request.getTargetUserAlias(), request.getAuthToken()))
            throw new RuntimeException("[AuthError] unauthenticated request");

        Pair<List<Status>, Boolean> pair = storyDAO.getStatuses(request.getTargetUserAlias(), request.getLimit(), request.getLastPost());
        return new StoryResponse(pair.getFirst(), pair.getSecond());
    }

}
