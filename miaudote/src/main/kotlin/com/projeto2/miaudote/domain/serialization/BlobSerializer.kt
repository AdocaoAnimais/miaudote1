package com.projeto2.miaudote.domain.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.hibernate.engine.jdbc.BlobProxy
import java.io.IOException

class BlobSerializer : JsonSerializer<BlobProxy>() {

    @Throws(IOException::class)
    override fun serialize(value: BlobProxy?, gen: JsonGenerator, serializers: SerializerProvider) {
        if (value != null) {
            val bytes = value.binaryStream.readBytes()
            gen.writeBinary(bytes)
        } else {
            gen.writeNull()
        }
    }
}