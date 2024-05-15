package com.projeto2.miaudote.enums.converters

import com.projeto2.miaudote.enums.Castrado
import com.projeto2.miaudote.enums.toCastrado
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class CastradoConverter : AttributeConverter<Castrado?, String?> {
    override fun convertToDatabaseColumn(castrado: Castrado?): String? {
        return castrado?.id
    }

    override fun convertToEntityAttribute(id: String?): Castrado? {
        return id?.toCastrado()
    }
}