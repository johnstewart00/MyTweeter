package edu.byu.cs.tweeter.server.dao.interfaces;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.unFollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.unFollowResponse;
import edu.byu.cs.tweeter.util.Pair;

public interface FollowDAOInterface {
    Pair<List<User>, Boolean> getFollowees(String followerAlias, int limit, String lastFolloweeAlias);

    Pair<List<User>, Boolean> getFollowers(String followeeAlias, int limit, String lastFollowerAlias);

    FollowersCountResponse getFollowerCount(FollowersCountRequest request);

    FollowingCountResponse getFollowingCount(FollowingCountRequest request);

    FollowResponse followTask(FollowRequest request);

    unFollowResponse unfollowTask(unFollowRequest request);

    IsFollowerResponse isFollower(IsFollowerRequest request);
}
