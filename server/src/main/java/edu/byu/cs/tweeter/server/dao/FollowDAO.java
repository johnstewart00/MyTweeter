package edu.byu.cs.tweeter.server.dao;

import com.google.inject.Inject;

import java.util.ArrayList;
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
import edu.byu.cs.tweeter.server.dao.accessHelpers.FollowBean;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAOInterface;
import edu.byu.cs.tweeter.server.dao.resultPages.DataPage;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

/**
 * A ParentDAO for accessing 'following' data from the database.
 */
public class FollowDAO extends ParentDAO implements FollowDAOInterface {
    private static final String TableName = "follows";
    public static final String IndexName = "follows_index";

    private static final String FollowerAttr = "follower_handle";
    private static final String FolloweeAttr = "followee_handle";
    private UserDAOInterface userDAO;
    // DynamoDB client
    @Inject
    public FollowDAO(UserDAOInterface userDAO){
        super();
        setUserDAO(userDAO);
        table = enhancedClient.table(TableName, TableSchema.fromBean(FollowBean.class));
    }

    public UserDAOInterface getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAOInterface userDAO) {
        this.userDAO = userDAO;
    }

    private Integer count = 0;

    /**
     * Gets the count of users from the database that the user specified is following. The
     * current implementation uses generated data and doesn't actually access a database.
     *
     * @param request the User whose count of how many following is desired.
     * @return said count.
     */
    @Override
    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
        String lastFollowee = null;
        List<User> followees = new ArrayList<>();
        Pair<List<User>, Boolean> followeesPair = null;
        Boolean getMorePages = true;
        while (getMorePages) {
            followeesPair = getFollowees(request.getFollower().getAlias(), 25, lastFollowee);
            followees.addAll(followeesPair.getFirst());
            lastFollowee = followeesPair.getFirst().get(followeesPair.getFirst().size()-1).getAlias();
            getMorePages = followeesPair.getSecond();
        }
        return new FollowingCountResponse(followees.size());
    }

    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param targetUserAlias the alias of the user whose followees are to be returned
     * @param pageSize the number of followees to be returned in one page
     * @param lastUserAlias the alias of the last followee in the previously retrieved page or
     *                          null if there was no previous request.
     * @return the followees.
     */
    @Override
    public Pair<List<User>, Boolean> getFollowees(String targetUserAlias, int pageSize, String lastUserAlias) {

        List<User> allFollowees = getAllFollowees(targetUserAlias);

        boolean hasMorePages = false;
        List<User> responseFollowees = new ArrayList<>(pageSize);
        if(pageSize > 0) {
            if (allFollowees != null) {
                int followeesIndex = getFollowsStartingIndex(lastUserAlias, allFollowees);

                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < pageSize; followeesIndex++, limitCounter++) {
                    responseFollowees.add(allFollowees.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allFollowees.size();
            }
        }
        return new Pair<>(responseFollowees, hasMorePages);
    }

    /**
     * Determines the index for the first followee in the specified 'allFollowees' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastFollowAlias the alias of the last followee that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allFollows the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getFollowsStartingIndex(String lastFollowAlias, List<User> allFollows) {

        int followsIndex = 0;

        if(lastFollowAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollows.size(); i++) {
                if(lastFollowAlias.equals(allFollows.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followsIndex = i + 1;
                    break;
                }
            }
        }

        return followsIndex;
    }
    @Override
    public Pair<List<User>, Boolean> getFollowers(String targetUserAlias, int pageSize, String lastUserAlias) {
        List<User> allFollowers = getAllFollowers(targetUserAlias);
        List<User> responseFollowers = new ArrayList<>(pageSize);

        boolean hasMorePages = false;

        if(pageSize > 0) {
            if (allFollowers != null) {
                int followersIndex = getFollowsStartingIndex(lastUserAlias, allFollowers);

                for(int limitCounter = 0; followersIndex < allFollowers.size() && limitCounter < pageSize; followersIndex++, limitCounter++) {
                    responseFollowers.add(allFollowers.get(followersIndex));
                }

                hasMorePages = followersIndex < allFollowers.size();
            }
        }

        return new Pair<>(responseFollowers, hasMorePages);
    }

    @Override
    public FollowersCountResponse getFollowerCount(FollowersCountRequest request) {
        String lastFollower = null;
        List<User> followers = new ArrayList<>();
        Pair<List<User>, Boolean> followersPair = null;
        Boolean getMorePages = true;
        while (getMorePages) {
            followersPair = getFollowers(request.getFollowee().getAlias(), 25, lastFollower);
            followers.addAll(followersPair.getFirst());
            lastFollower = followersPair.getFirst().get(followersPair.getFirst().size()-1).getAlias();
            getMorePages = followersPair.getSecond();
        }
        return new FollowersCountResponse(followers.size());
    }
    @Override
    public FollowResponse followTask(FollowRequest request) {
        FollowBean followBean = new FollowBean(request.getCurrUser().getAlias(), request.getCurrUser().getFirstName(),
                request.getTargetUser().getAlias(), request.getTargetUser().getFirstName());
        table.putItem(followBean);
        return new FollowResponse(true);
    }
    @Override
    public unFollowResponse unfollowTask(unFollowRequest request) {
        Key key = Key.builder()
                .partitionValue(request.getCurrUser().getAlias()).sortValue(request.getTargetUser().getAlias())
                .build();
        table.deleteItem(key);
        return new unFollowResponse(true);
    }
    @Override
    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        IsFollowerResponse response = new IsFollowerResponse(true);
        List<User> followees = getAllFollowees(request.getFollower().getAlias());
        if (followees.contains(request.getFollowee())){
            response.setIsFollower(true);
        } else {
            response.setIsFollower(false);
        }
        return response;
    }

    private List<User> getAllFollowees(String targetUserAlias){
        DynamoDbTable<FollowBean> index = enhancedClient.table(TableName, TableSchema.fromBean(FollowBean.class));
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key));

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<FollowBean> result = new DataPage<FollowBean>();

        SdkIterable<Page<FollowBean>> sdkIterable = index.query(request);
        PageIterable<FollowBean> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowBean> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(visit -> result.getValues().add(visit));
                });

        List<FollowBean> allFolloweesBean = result.getValues();
        List<User> allFollowees = new ArrayList<>();
        for (int i=0; i<allFolloweesBean.size(); i++){
            User user = userDAO.getUser(allFolloweesBean.get(i).getFollowee_handle());
            allFollowees.add(user);
        }
        return allFollowees;
    }
    private List<User> getAllFollowers(String targetUserAlias){
        DynamoDbIndex<FollowBean> index = enhancedClient.table(TableName, TableSchema.fromBean(FollowBean.class)).index(IndexName);
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key));

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<FollowBean> result = new DataPage<FollowBean>();

        SdkIterable<Page<FollowBean>> sdkIterable = index.query(request);
        PageIterable<FollowBean> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowBean> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(visit -> result.getValues().add(visit));
                });

        List<FollowBean> allFollowersBean = result.getValues();
        List<User> allFollowers = new ArrayList<>();
        for (int i=0; i<allFollowersBean.size(); i++){
            User user = userDAO.getUser(allFollowersBean.get(i).getFollower_handle());
            allFollowers.add(user);
        }
        return allFollowers;
    }
}
