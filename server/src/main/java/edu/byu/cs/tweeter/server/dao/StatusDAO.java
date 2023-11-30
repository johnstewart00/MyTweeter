package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusReponse;
import edu.byu.cs.tweeter.server.dao.accessHelpers.FeedBean;
import edu.byu.cs.tweeter.server.dao.accessHelpers.StoryBean;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class StatusDAO extends ParentDAO {
    // don't set a constant for the table because we will need to access two tables, feed and story possibly more
    public PostStatusReponse postStatus(PostStatusRequest request) {

        table = enhancedClient.table("story", TableSchema.fromBean(StoryBean.class));
        StoryBean storyBean = new StoryBean(request.getPost().getPost(), request.getCurrUser().getAlias(),
                request.getPost().getTimestamp(), request.getPost().getUrls(), request.getPost().getMentions());
        table.putItem(storyBean);
        table = enhancedClient.table("feed", TableSchema.fromBean(FeedBean.class));
        FollowDAO followDAO = new FollowDAO();
        String lastFollower = null;
        List<User> followers = new ArrayList<>();
        Pair<List<User>, Boolean> followersPair = null;
        Boolean getMorePages = true;
        while (getMorePages) {
            followersPair = followDAO.getFollowers(request.getCurrUser().getAlias(), 25, lastFollower);
            followers.addAll(followersPair.getFirst());
            lastFollower = followersPair.getFirst().get(followersPair.getFirst().size()-1).getAlias();
            getMorePages = followersPair.getSecond();
        }
        for (int i=0; i<followers.size(); i++){
            FeedBean feedBean = new FeedBean(request.getPost().getPost(), followers.get(i).getAlias(), request.getCurrUser().getAlias(),
                    request.getPost().getTimestamp(), request.getPost().getUrls(), request.getPost().getMentions());
            table.putItem(feedBean);
        }
        return new PostStatusReponse(true);
    }
}
