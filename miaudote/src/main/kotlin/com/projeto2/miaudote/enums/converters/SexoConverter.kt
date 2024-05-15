package com.projeto2.miaudote.enums.converters

import com.projeto2.miaudote.enums.Sexo
import com.projeto2.miaudote.enums.toSexo
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class SexoConverter : AttributeConverter<Sexo?, String?> {
    override fun convertToDatabaseColumn(sexo: Sexo?): String? {
        return sexo?.id
    }

    override fun convertToEntityAttribute(id: String?): Sexo? {
        return id?.toSexo()
    }
}