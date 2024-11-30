package com.projeto2.miaudote.domain.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.hibernate.Hibernate
import org.hibernate.engine.jdbc.BlobProxy
import java.io.ByteArrayInputStream
import java.io.IOException
import java.sql.Blob
/**
 * Desserializa um objeto Blob a partir de uma representação JSON.
 * A conversão é feita lendo os bytes do JSON e criando um objeto Blob com esses bytes.
 */
class BlobDeserializer : JsonDeserializer<Blob>() {
    /**
     * Desserializa um Blob a partir de JSON.
     * Lê os bytes do JSON e os converte em um Blob usando um stream de bytes.
     *
     * @param p O parser de JSON utilizado para extrair os dados.
     * @param ctxt O contexto de desserialização.
     * @return O objeto Blob gerado a partir dos bytes lidos do JSON, ou null se não for possível desserializar.
     * @throws IOException Se ocorrer um erro durante a leitura dos dados JSON.
     */
    @Throws(IOException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Blob? {
        val bytes = p.codec.readValue(p, ByteArray::class.java)
        val inputStream = ByteArrayInputStream(bytes)
        return BlobProxy.generateProxy(inputStream, bytes.size.toLong())
    }
}