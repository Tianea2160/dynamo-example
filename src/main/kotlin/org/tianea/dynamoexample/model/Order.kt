package org.tianea.dynamoexample.model

import com.amazonaws.services.dynamodbv2.datamodeling.*
import java.math.BigDecimal
import java.time.LocalDateTime

@DynamoDBTable(tableName = "orders")
data class Order(
    @DynamoDBHashKey
    var userId: String? = null,
    
    @DynamoDBRangeKey
    var orderId: String? = null,
    
    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter::class)
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "status-index")
    var orderDate: LocalDateTime? = null,
    
    @DynamoDBAttribute
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "status-index")
    var status: OrderStatus = OrderStatus.PENDING,
    
    @DynamoDBAttribute
    var items: List<OrderItem> = emptyList(),
    
    @DynamoDBAttribute
    var totalAmount: BigDecimal = BigDecimal.ZERO
)

@DynamoDBDocument
data class OrderItem(
    var productId: String? = null,
    var productName: String? = null,
    var quantity: Int = 0,
    var price: BigDecimal = BigDecimal.ZERO
)

enum class OrderStatus {
    PENDING, PAID, SHIPPED, DELIVERED, CANCELLED
}

class LocalDateTimeConverter : DynamoDBTypeConverter<String, LocalDateTime> {
    override fun convert(date: LocalDateTime): String {
        return date.toString()
    }

    override fun unconvert(value: String): LocalDateTime {
        return LocalDateTime.parse(value)
    }
} 