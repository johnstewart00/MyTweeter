package edu.byu.cs.tweeter.server.service;

import com.google.inject.Inject;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.StoryDAOInterface;
import edu.byu.cs.tweeter.util.Pair;

public class StoryService extends Service {
    private StoryDAOInterface storyDAO;
    private AuthTokenDAOInterface authTokenDAO;

    @Inject
    public StoryService(StoryDAOInterface storyDAO, AuthTokenDAOInterface authTokenDAO){
        super(authTokenDAO);
        setStoryDAO(storyDAO);
    }

    public StoryResponse getStory(StoryRequest request) {
        checkRequest(request);
        checkNull(request.getTargetUserAlias(), NO_TARGET_USER);
        checkLimit(request.getLimit());
        checkAuthToken(request.getTargetUserAlias(), request.getAuthToken());

        Pair<List<Status>, Boolean> pair = storyDAO.getStatuses(request.getTargetUserAlias(), request.getLimit(), request.getLastPost());
        return new StoryResponse(pair.getFirst(), pair.getSecond());
    }
    public StoryDAOInterface getStoryDAO() {
        return storyDAO;
    }

    public void setStoryDAO(StoryDAOInterface storyDAO) {
        this.storyDAO = storyDAO;
    }

}
