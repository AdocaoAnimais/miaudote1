package com.projeto2.miaudote.domain.enums.converters

import com.projeto2.miaudote.domain.enums.Tipo
import com.projeto2.miaudote.domain.enums.toTipo
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class TipoConverter : AttributeConverter<Tipo?, String?> {
    override fun convertToDatabaseColumn(tipo: Tipo?): String? {
        return tipo?.id
    }

    override fun convertToEntityAttribute(id: String?): Tipo? {
        return id?.toTipo()?.getOrNull()
    }
}