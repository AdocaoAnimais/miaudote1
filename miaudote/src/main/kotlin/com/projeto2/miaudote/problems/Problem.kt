package com.projeto2.miaudote.problems

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import java.net.URI

class Problem(
    val title: String,
    val detail: String,
    val status: Int,
    val type: URI,
    val extra: Map<String, Any?>?,
) : Exception() {
    constructor(
        title: String,
        detail: String,
        status: HttpStatus,
        type: URI,
        extra: Map<String, String?>?,
    ) : this(
        title = title,
        detail = detail,
        status = status.value(),
        type = type,
        extra = extra
    )

    fun getProblemDetail(): ProblemDetail {
        val problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, this.title)
        problem.detail = this.detail
        problem.title = this.title
        problem.type = this.type
        problem.status = this.status
        problem.properties = this.extra
        return problem
    }
}

fun Problem.toFailure(): Result<Any> = Result.failure(this)
