package org.tianea.dynamoexample.model

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable

@DynamoDBTable(tableName = "users")
data class User(
    @DynamoDBHashKey
    var id: String? = null,
    
    @DynamoDBAttribute
    var name: String? = null,
    
    @DynamoDBAttribute
    var email: String? = null,
    
    @DynamoDBAttribute
    var age: Int? = null
) 