package com.projeto2.miaudote.domain.enums.converters

import com.projeto2.miaudote.domain.enums.Porte
import com.projeto2.miaudote.domain.enums.toPorte
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
/**
 * Converte o valor da enumeração Porte para o formato de string no banco de dados e vice-versa.
 *
 * @see Porte
 */
@Converter(autoApply = true)
class PorteConverter : AttributeConverter<Porte?, String?> {
    /**
     * Converte o valor de Porte para o formato de string para armazenamento no banco de dados.
     *
     * @param porte O valor da enumeração Porte a ser convertido.
     * @return O identificador do Porte ou null se o valor for nulo.
     */
    override fun convertToDatabaseColumn(porte: Porte?): String? {
        return porte?.id
    }
    /**
     * Converte uma string armazenada no banco de dados para o valor correspondente da enumeração Porte.
     *
     * @param id O identificador em formato string a ser convertido para Porte.
     * @return O valor da enumeração Porte correspondente ou null se o identificador for inválido.
     */
    override fun convertToEntityAttribute(id: String?): Porte? {
        return id?.toPorte()?.getOrNull()
    }
}