package com.projeto2.miaudote

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class MiaudoteApplication // classe que tem a aplicação inteira

fun main(args: Array<String>) {
	runApplication<MiaudoteApplication>(*args)
}
