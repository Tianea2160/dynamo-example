package org.tianea.dynamoexample.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableDynamoDBRepositories(basePackages = ["org.tianea.dynamoexample.repository"])
@EnableConfigurationProperties(DynamoDBProperties::class)
class DynamoDBConfig(
    private val dynamoDBProperties: DynamoDBProperties
) {

    @Bean
    fun amazonDynamoDB(): AmazonDynamoDB {
        return AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(
                    dynamoDBProperties.endpoint,
                    dynamoDBProperties.region
                )
            )
            .withCredentials(
                AWSStaticCredentialsProvider(
                    BasicAWSCredentials(
                        dynamoDBProperties.credentials.accessKey,
                        dynamoDBProperties.credentials.secretKey
                    )
                )
            )
            .build()
    }
}

@ConfigurationProperties(prefix = "aws.dynamodb")
data class DynamoDBProperties(
    val endpoint: String,
    val region: String,
    val credentials: Credentials
) {
    data class Credentials(
        val accessKey: String,
        val secretKey: String
    )
} 