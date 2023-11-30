package edu.byu.cs.tweeter.server.dao.accessHelpers;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class FollowBean {
    public static final String IndexName="follows_index";
    private String follower_handle;
    private String follower_name;
    private String followee_handle;
    private String followee_name;

    public FollowBean(){};
    public FollowBean(String follower_handle, String follower_name, String followee_handle, String followee_name){
        this.follower_handle = follower_handle;
        this.follower_name = follower_name;
        this.followee_handle = followee_handle;
        this.followee_name = followee_name;
    }

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames=IndexName)
    public String getFollower_handle( ) {
        return follower_handle;
    }

    public void setFollower_handle(String follower_handle) {
        this.follower_handle=follower_handle;
    }

    public String getFollower_name( ) {
        return follower_name;
    }

    public void setFollower_name(String follower_name) {
        this.follower_name=follower_name;
    }
    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = IndexName)
    public String getFollowee_handle( ) {
        return followee_handle;
    }

    public void setFollowee_handle(String followee_handle) {
        this.followee_handle=followee_handle;
    }

    public String getFollowee_name( ) {
        return followee_name;
    }

    public void setFollowee_name(String followee_name) {
        this.followee_name=followee_name;
    }
}
