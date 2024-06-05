package com.projeto2.miaudote.domain.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.hibernate.engine.jdbc.BlobProxy
import java.io.IOException
import java.sql.Blob

class BlobSerializer : JsonSerializer<Blob>() {

    @Throws(IOException::class)
    override fun serialize(value: Blob?, gen: JsonGenerator, serializers: SerializerProvider) {
        if (value != null) {
            val bytes = value.binaryStream.readBytes()
            gen.writeBinary(bytes)
        } else {
            gen.writeNull()
        }
    }
}