package com.projeto2.miaudote.domain.enums.converters

import com.projeto2.miaudote.domain.enums.Sexo
import com.projeto2.miaudote.domain.enums.toSexo
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
/**
 * Converte o valor da enumeração Sexo para o formato de string no banco de dados e vice-versa.
 *
 * @see Sexo
 */
@Converter(autoApply = true)
class SexoConverter : AttributeConverter<Sexo?, String?> {
    /**
     * Converte o valor de Sexo para o formato de string para armazenamento no banco de dados.
     *
     * @param sexo O valor da enumeração Sexo a ser convertido.
     * @return O identificador do Sexo ou null se o valor for nulo.
     */
    override fun convertToDatabaseColumn(sexo: Sexo?): String? {
        return sexo?.id
    }
    /**
     * Converte uma string armazenada no banco de dados para o valor correspondente da enumeração Sexo.
     *
     * @param id O identificador em formato string a ser convertido para Sexo.
     * @return O valor da enumeração Sexo correspondente ou null se o identificador for inválido.
     */
    override fun convertToEntityAttribute(id: String?): Sexo? {
        return id?.toSexo()?.getOrNull()
    }
}