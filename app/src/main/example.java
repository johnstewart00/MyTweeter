import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;

public class DynamoDBQueryExample {

    public static void main(String[] args) {
        // Create a DynamoDB client
        DynamoDbClient ddb = DynamoDbClient.create();

        // Define the table name
        String tableName = "YourTableName";

        // Define the partition key value
        String partitionKeyValue = "YourPartitionKeyValue"; // Replace with your actual value

        // Define the filter expression for the non-sort key column
        String filterExpression = "yourNonSortKeyColumn = :nonSortKeyVal";

        // Define the expression attribute values for filter expression
        Map<String, AttributeValue> filterExpressionAttributeValues = new HashMap<>();
        filterExpressionAttributeValues.put(":nonSortKeyVal", AttributeValue.builder().s("YourDesiredValue").build()); // Replace with your desired value

        // Define the sort key condition expression
        String sortKeyConditionExpression = "timestamp > :val";

        // Define the expression attribute values for sort key condition
        Map<String, AttributeValue> sortKeyExpressionAttributeValues = new HashMap<>();
        sortKeyExpressionAttributeValues.put(":val", AttributeValue.builder().n("YourDesiredTimestamp").build()); // Replace with your actual timestamp

        // Define the query request
        QueryRequest queryRequest = QueryRequest.builder()
                .tableName(tableName)
                .keyConditionExpression("alias = :partitionKey and " + sortKeyConditionExpression)
                .filterExpression(filterExpression)
                .expressionAttributeValues(
                        // Combine both filter and sort key expression attribute values
                        new HashMap<>(filterExpressionAttributeValues),
                        new HashMap<>(sortKeyExpressionAttributeValues)
                )
                .build();

        // Set ExclusiveStartKey to start the query from a specific item
        Map<String, AttributeValue> exclusiveStartKey = new HashMap<>();
        exclusiveStartKey.put("alias", AttributeValue.builder().s(partitionKeyValue).build());
        exclusiveStartKey.put("timestamp", AttributeValue.builder().n("YourDesiredTimestampToStartFrom").build()); // Replace with your desired timestamp to start from
        queryRequest = queryRequest.toBuilder().exclusiveStartKey(exclusiveStartKey).build();

        // Perform the query
        try {
            QueryResponse queryResponse = ddb.query(queryRequest);
            // Process the query results
            System.out.println("Query results: " + queryResponse.items());

            // If there are more items to retrieve, use LastEvaluatedKey for pagination
            if (queryResponse.hasLastEvaluatedKey()) {
                Map<String, AttributeValue> lastEvaluatedKey = queryResponse.lastEvaluatedKey();
                System.out.println("LastEvaluatedKey: " + lastEvaluatedKey);
            }

        } catch (DynamoDbException e) {
            // Handle exceptions
            System.err.println(e.getMessage());
        }
    }
}

