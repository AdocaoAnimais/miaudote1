package com.projeto2.miaudote.domain.enums.converters

import com.projeto2.miaudote.domain.enums.Castrado
import com.projeto2.miaudote.domain.enums.toCastrado
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
/**
 * Converte o valor da enumeração Castrado para o formato de string no banco de dados e vice-versa.
 *
 * @see Castrado
 */
@Converter(autoApply = true)
class CastradoConverter : AttributeConverter<Castrado?, String?> {
    /**
     * Converte o valor de Castrado para o formato de string para armazenamento no banco de dados.
     *
     * @param castrado O valor da enumeração Castrado a ser convertido.
     * @return O identificador do Castrado ou null se o valor for nulo.
     */
    override fun convertToDatabaseColumn(castrado: Castrado?): String? {
        return castrado?.id
    }
    /**
     * Converte uma string armazenada no banco de dados para o valor correspondente da enumeração Castrado.
     *
     * @param id O identificador em formato string a ser convertido para Castrado.
     * @return O valor da enumeração Castrado correspondente ou null se o identificador for inválido.
     */
    override fun convertToEntityAttribute(id: String?): Castrado? {
        return id?.toCastrado()?.getOrNull()
    }
}