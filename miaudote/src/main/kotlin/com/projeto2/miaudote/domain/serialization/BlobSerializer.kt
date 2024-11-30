package com.projeto2.miaudote.domain.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.hibernate.engine.jdbc.BlobProxy
import java.io.IOException
import java.sql.Blob

/**
 * Serializa um objeto Blob para sua representação JSON.
 * Converte os bytes do Blob em uma representação binária no formato JSON.
 */
class BlobSerializer : JsonSerializer<Blob>() {
    /**
     * Serializa um Blob para JSON.
     * Converte os bytes do Blob em uma sequência binária no JSON.
     * Caso o valor do Blob seja null, será escrito um valor null no JSON.
     *
     * @param value O objeto Blob a ser serializado.
     * @param gen O gerador de JSON utilizado para escrever a representação.
     * @param serializers O provedor de serializadores.
     * @throws IOException Se ocorrer um erro durante a escrita dos dados no JSON.
     */
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