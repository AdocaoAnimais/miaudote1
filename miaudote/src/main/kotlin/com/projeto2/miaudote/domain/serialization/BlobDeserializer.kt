package com.projeto2.miaudote.domain.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.hibernate.Hibernate
import org.hibernate.engine.jdbc.BlobProxy
import java.io.ByteArrayInputStream
import java.io.IOException

class BlobDeserializer : JsonDeserializer<BlobProxy>() {

    @Throws(IOException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): BlobProxy? {
        val bytes = p.codec.readValue(p, ByteArray::class.java)
        val inputStream = ByteArrayInputStream(bytes)
        return BlobProxy.generateProxy(inputStream, bytes.size.toLong()) as BlobProxy?
    }
}