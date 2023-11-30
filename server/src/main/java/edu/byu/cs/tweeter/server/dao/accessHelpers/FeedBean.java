package edu.byu.cs.tweeter.server.dao.accessHelpers;

import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class FeedBean {
    private String post;
    private String alias; // user to be seen status

    private String aliasCreator;
    private Long timestamp;
    private List<String> urls;
    private List<String> mentions;

    public FeedBean(){}
    public FeedBean(String post, String alias, String aliasCreator,Long timestamp, List<String> urls, List<String> mentions ){
        this.post = post;
        this.alias = alias;
        this.aliasCreator = aliasCreator;
        this.timestamp = timestamp;
        this.urls = urls;
        this.mentions = mentions;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @DynamoDbPartitionKey
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
    @DynamoDbSortKey
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }
    public String getAliasCreator() {
        return aliasCreator;
    }

    public void setAliasCreator(String aliasCreator) {
        this.aliasCreator = aliasCreator;
    }

}
