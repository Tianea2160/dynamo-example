package org.tianea.dynamoexample.controller

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.tianea.dynamoexample.model.Order
import org.tianea.dynamoexample.model.OrderItem
import org.tianea.dynamoexample.model.OrderStatus
import org.tianea.dynamoexample.service.OrderService
import java.math.BigDecimal
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService
) {
    @PostMapping
    fun createOrder(@RequestBody request: CreateOrderRequest): ResponseEntity<Order> {
        val order = orderService.createOrder(
            userId = request.userId,
            items = request.items.map { it.toOrderItem() }
        )
        return ResponseEntity.ok(order)
    }

    @GetMapping("/user/{userId}")
    fun getOrdersByUser(@PathVariable userId: String): ResponseEntity<List<Order>> {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId))
    }

    @GetMapping("/status/{status}")
    fun getOrdersByStatus(
        @PathVariable status: OrderStatus
    ): ResponseEntity<List<Order>> {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status))
    }

    @GetMapping("/status/{status}/date-range")
    fun getOrdersByStatusAndDateRange(
        @PathVariable status: OrderStatus,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: LocalDateTime
    ): ResponseEntity<List<Order>> {
        return ResponseEntity.ok(
            orderService.getOrdersByStatusAndDateRange(status, startDate, endDate)
        )
    }

    @GetMapping("/user/{userId}/date-range")
    fun getUserOrdersByDateRange(
        @PathVariable userId: String,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: LocalDateTime
    ): ResponseEntity<List<Order>> {
        return ResponseEntity.ok(
            orderService.getUserOrdersByDateRange(userId, startDate, endDate)
        )
    }

    @PutMapping("/{userId}/{orderId}/status")
    fun updateOrderStatus(
        @PathVariable userId: String,
        @PathVariable orderId: String,
        @RequestBody request: UpdateOrderStatusRequest
    ): ResponseEntity<Order> {
        return orderService.updateOrderStatus(userId, orderId, request.status)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }
}

data class CreateOrderRequest(
    val userId: String,
    val items: List<OrderItemRequest>
)

data class OrderItemRequest(
    val productId: String,
    val productName: String,
    val quantity: Int,
    val price: BigDecimal
) {
    fun toOrderItem() = OrderItem(
        productId = productId,
        productName = productName,
        quantity = quantity,
        price = price
    )
}

data class UpdateOrderStatusRequest(
    val status: OrderStatus
) 