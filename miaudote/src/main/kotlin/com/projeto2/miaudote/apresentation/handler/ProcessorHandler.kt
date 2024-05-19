package com.projeto2.miaudote.apresentation.handler

import org.springframework.stereotype.Service

@Service
abstract class ProcessorHandler<T> {
    abstract fun process(handler: T): Result<Any>
}