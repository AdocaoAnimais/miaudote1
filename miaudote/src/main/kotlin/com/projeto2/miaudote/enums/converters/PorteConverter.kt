package com.projeto2.miaudote.enums.converters

import com.projeto2.miaudote.enums.Porte
import com.projeto2.miaudote.enums.toPorte
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class PorteConverter : AttributeConverter<Porte?, String?> {
    override fun convertToDatabaseColumn(porte: Porte?): String? {
        return porte?.id
    }

    override fun convertToEntityAttribute(id: String?): Porte? {
        return id?.toPorte()
    }
}