package edu.byu.cs.tweeter.server.service;

import com.google.inject.Inject;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.unFollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.unFollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAOInterface;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService extends Service{
    private FollowDAOInterface followDAO;

    @Inject
    public FollowService( FollowDAOInterface followDAO, AuthTokenDAOInterface authTokenDAO){
        super(authTokenDAO);
        setFollowDAO(followDAO);
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        checkRequest(request);
        checkNull(request.getFollowerAlias(), NO_FOLLOWER_ALIAS);
        checkLimit(request.getLimit());
        checkAuthToken(request.getFollowerAlias(), request.getAuthToken());
        Pair<List<User>, Boolean> pair = followDAO.getFollowees(request.getFollowerAlias(), request.getLimit(), request.getLastFolloweeAlias());
        return new FollowingResponse(pair.getFirst(), pair.getSecond());
    }

    /**
     * Returns an instance of {@link FollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */

    public FollowersResponse getFollowers(FollowersRequest request) {
        checkRequest(request);
        checkNull(request.getFolloweeAlias(), NO_FOLLOWEE_ALIAS);
        checkLimit(request.getLimit());
        checkAuthToken(request.getFolloweeAlias(), request.getAuthToken());

        Pair<List<User>, Boolean> pair = followDAO.getFollowers(request.getFolloweeAlias(), request.getLimit(), request.getLastFollowerAlias());
        return new FollowersResponse(pair.getFirst(), pair.getSecond());
    }


    public FollowersCountResponse getFollowersCount(FollowersCountRequest request) {
        checkRequest(request);
        checkNull(request.getFollowee(), NO_FOLLOWEE);
        checkAuthToken(request.getFollowee().getAlias(), request.getAuthToken());

        return followDAO.getFollowerCount(request);
    }

    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
        checkRequest(request);
        checkNull(request.getFollower(), NO_FOLLOWER);
        checkAuthToken(request.getFollower().getAlias(), request.getAuthToken());

        return followDAO.getFollowingCount(request);
    }

    public FollowResponse follow(FollowRequest request) {
        checkRequest(request);
        checkNull(request.getCurrUser(), NO_CURRENT_USER);
        checkNull(request.getTargetUser(), NO_TARGET_USER);
        checkAuthToken(request.getCurrUser().getAlias(), request.getAuthToken());

        return followDAO.followTask(request);
    }

    public unFollowResponse unfollow(unFollowRequest request) {
        checkRequest(request);
        checkNull(request.getCurrUser(), NO_CURRENT_USER);
        checkNull(request.getTargetUser(), NO_TARGET_USER);
        checkAuthToken(request.getCurrUser().getAlias(), request.getAuthToken());

        return followDAO.unfollowTask(request);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        checkRequest(request);
        checkNull(request.getFollower(), NO_FOLLOWER);
        checkNull(request.getFollowee(), NO_FOLLOWEE);
        checkAuthToken(request.getFollowee().getAlias(), request.getAuthToken());

        return followDAO.isFollower(request);
    }

    public FollowDAOInterface getFollowDAO() {
        return followDAO;
    }

    public void setFollowDAO(FollowDAOInterface followDAO) {
        this.followDAO = followDAO;
    }
}
