package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.server.dao.interfaces.DAO;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class ParentDAO<T> implements DAO {
    protected DynamoDbClient dynamoDbClient;

    protected DynamoDbEnhancedClient enhancedClient;
    protected DynamoDbTable<T> table;
    public ParentDAO(){
        if (dynamoDbClient == null || enhancedClient == null) {
            setDBClient();
            setEnhancedClient();
        }
    }

    private void setEnhancedClient() {
        enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    private void setDBClient() {
        dynamoDbClient = DynamoDbClient.builder()
                .region(Region.US_EAST_2)
                .build();
    }

    protected static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

}
