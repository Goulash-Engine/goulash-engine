package com.barbarus.prosper

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class HttpServer

fun main(args: Array<String>) {
    @Suppress("SpreadOperator")
    runApplication<HttpServer>(*args)
}
