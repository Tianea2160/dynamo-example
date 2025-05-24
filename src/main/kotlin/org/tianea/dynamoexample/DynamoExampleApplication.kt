package org.tianea.dynamoexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DynamoExampleApplication

fun main(args: Array<String>) {
    runApplication<DynamoExampleApplication>(*args)
}
