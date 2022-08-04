package com.barbarus.prosper

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
open class HttpServer

fun main(args: Array<String>) {
    SpringApplicationBuilder().bannerMode(Banner.Mode.OFF)
        .sources(HttpServer::class.java)
        .run(*args)
}
