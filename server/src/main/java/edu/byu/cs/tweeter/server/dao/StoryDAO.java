package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.accessHelpers.StoryBean;
import edu.byu.cs.tweeter.server.dao.interfaces.StoryDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAOInterface;
import edu.byu.cs.tweeter.server.dao.resultPages.DataPage;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;
import jakarta.inject.Inject;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

public class StoryDAO extends ParentDAO implements StoryDAOInterface {

    private UserDAOInterface userDAO;
    @Inject
    public StoryDAO(UserDAOInterface userDAO){
        super();
        setUserDAO(userDAO);
    }

    public UserDAOInterface getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAOInterface userDAO) {
        this.userDAO = userDAO;
    }

    private static final String TableName = "story";
    private static final String aliasAttr = "alias";
    private static final String lastPostAttr = "post";
    @Override
    public Pair<List<Status>, Boolean> getStatuses(String targetUserAlias, int limit, String lastPost) {

        DynamoDbTable<StoryBean> index = enhancedClient.table(TableName, TableSchema.fromBean(StoryBean.class));
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key));

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<StoryBean> result = new DataPage<StoryBean>();

        SdkIterable<Page<StoryBean>> sdkIterable = index.query(request);
        PageIterable<StoryBean> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<StoryBean> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(visit -> result.getValues().add(visit));
                });

        List<StoryBean> allStatusesBean = result.getValues();
        List<Status> allStatuses = new ArrayList<>();
        for(int i=0; i< allStatusesBean.size(); i++){
            StoryBean status = allStatusesBean.get(i);
            User user = userDAO.getUser(status.getAlias());
            allStatuses.add(new Status(status.getPost(), user, status.getTimestamp(), status.getUrls(), status.getMentions()));
        }
        Collections.reverse(allStatuses);
        System.out.println("All of the statuses returned and reversed are: ");
        for (int i = 0; i< allStatuses.size(); i++){
            System.out.println(allStatuses.get(i).getPost());
        }
        boolean hasMorePages = false;
        List<Status> responseStatuses = new ArrayList<>(limit);
        if(limit > 0) {
            if (allStatuses != null) {
                int statusesIndex = getStatusesStartingIndex(lastPost, allStatuses);

                for(int limitCounter = 0; statusesIndex < allStatuses.size() && limitCounter < limit; statusesIndex++, limitCounter++) {
                    responseStatuses.add(allStatuses.get(statusesIndex));
                }

                hasMorePages = statusesIndex < allStatuses.size();
            }
        }

        return new Pair<>(responseStatuses, hasMorePages);
    }

    private int getStatusesStartingIndex(String lastPost, List<Status> allStatuses) {

        int statusesIndex = 0;

        if(lastPost != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allStatuses.size(); i++) {
                if(lastPost.equals(allStatuses.get(i).getPost())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    statusesIndex = i + 1;
                    break;
                }
            }
        }

        return statusesIndex;
    }
}
