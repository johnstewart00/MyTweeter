package edu.byu.cs.tweeter.server.dao;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.accessHelpers.FeedBean;
import edu.byu.cs.tweeter.server.dao.interfaces.FeedDAOInterface;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAOInterface;
import edu.byu.cs.tweeter.server.dao.resultPages.DataPage;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

public class FeedDAO extends ParentDAO implements FeedDAOInterface {
    private static final String TableName = "feed";
    private static final String aliasAttr = "alias";
    private static final String lastPostAttr = "post";
    private UserDAOInterface userDAO;

    @Inject
    public FeedDAO (UserDAOInterface userDAO){
        super();
        setUserDAO(userDAO);
    }
    @Override
    public Pair<List<Status>, Boolean> getStatuses(String targetUserAlias, int pageSize, String lastPost) {
        DynamoDbTable<FeedBean> index = enhancedClient.table(TableName, TableSchema.fromBean(FeedBean.class));
        Key key = Key.builder()
                .partitionValue(targetUserAlias)
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key));

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<FeedBean> result = new DataPage<FeedBean>();

        SdkIterable<Page<FeedBean>> sdkIterable = index.query(request);
        PageIterable<FeedBean> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<FeedBean> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(visit -> result.getValues().add(visit));
                });
        List<FeedBean> allStatusesBean = result.getValues();
        List<Status> allStatuses = new ArrayList<>();
        for (int i=0; i<allStatusesBean.size(); i++){
            FeedBean status = allStatusesBean.get(i);
            User user = userDAO.getUser(status.getAliasCreator());
            allStatuses.add(new Status(status.getPost(), user, status.getTimestamp(), status.getUrls(), status.getMentions()));
        }
        Collections.reverse(allStatuses);
        boolean hasMorePages = false;
        List<Status> responseStatuses = new ArrayList<>(pageSize);

        if(pageSize > 0) {
            if (allStatuses != null) {
                int statusesIndex = getStatusesStartingIndex(lastPost, allStatuses);

                for(int limitCounter = 0; statusesIndex < allStatuses.size() && limitCounter < pageSize; statusesIndex++, limitCounter++) {
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
    public UserDAOInterface getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAOInterface userDAO) {
        this.userDAO = userDAO;
    }
}
