package com.projeto2.miaudote.domain.enums.converters

import com.projeto2.miaudote.domain.enums.Tipo
import com.projeto2.miaudote.domain.enums.toTipo
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
/**
 * Converte o valor da enumeração Tipo para o formato de string no banco de dados e vice-versa.
 *
 * @see Tipo
 */
@Converter(autoApply = true)
class TipoConverter : AttributeConverter<Tipo?, String?> {
    /**
     * Converte o valor de Tipo para o formato de string para armazenamento no banco de dados.
     *
     * @param tipo O valor da enumeração Tipo a ser convertido.
     * @return O identificador do Tipo ou null se o valor for nulo.
     */
    override fun convertToDatabaseColumn(tipo: Tipo?): String? {
        return tipo?.id
    }

    /**
     * Converte uma string armazenada no banco de dados para o valor correspondente da enumeração Tipo.
     *
     * @param id O identificador em formato string a ser convertido para Tipo.
     * @return O valor da enumeração Tipo correspondente ou null se o identificador for inválido.
     */
    override fun convertToEntityAttribute(id: String?): Tipo? {
        return id?.toTipo()?.getOrNull()
    }
}