package com.projeto2.miaudote.domain.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.hibernate.Hibernate
import org.hibernate.engine.jdbc.BlobProxy
import java.io.ByteArrayInputStream
import java.io.IOException
import java.sql.Blob

class BlobDeserializer : JsonDeserializer<Blob>() {

    @Throws(IOException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Blob? {
        val bytes = p.codec.readValue(p, ByteArray::class.java)
        val inputStream = ByteArrayInputStream(bytes)
        return BlobProxy.generateProxy(inputStream, bytes.size.toLong())
    }
}