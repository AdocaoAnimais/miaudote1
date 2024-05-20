package com.projeto2.miaudote.application.handler

import org.springframework.stereotype.Service

@Service
abstract class ProcessorHandler<T: RequestHandler> {
    abstract fun process(handler: T): Result<Any>
}