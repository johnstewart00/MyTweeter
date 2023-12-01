package edu.byu.cs.tweeter.server.dao;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusReponse;
import edu.byu.cs.tweeter.server.dao.accessHelpers.FeedBean;
import edu.byu.cs.tweeter.server.dao.accessHelpers.StoryBean;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.StatusDAOInterface;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class StatusDAO extends ParentDAO implements StatusDAOInterface {
    private FollowDAOInterface followDAO;

    @Inject
    public StatusDAO(FollowDAOInterface followDAO){
        super();
        setFollowDAO(followDAO);
    }

    public FollowDAOInterface getFollowDAO() {
        return followDAO;
    }

    public void setFollowDAO(FollowDAOInterface followDAO) {
        this.followDAO = followDAO;
    }

    @Override
    public PostStatusReponse postStatus(PostStatusRequest request) {

        table = enhancedClient.table("story", TableSchema.fromBean(StoryBean.class));
        StoryBean storyBean = new StoryBean(request.getPost().getPost(), request.getCurrUser().getAlias(),
                request.getPost().getTimestamp(), request.getPost().getUrls(), request.getPost().getMentions());
        table.putItem(storyBean);
        table = enhancedClient.table("feed", TableSchema.fromBean(FeedBean.class));
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
