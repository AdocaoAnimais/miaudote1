package com.projeto2.miaudote.enums.converters

import com.projeto2.miaudote.enums.Tipo
import com.projeto2.miaudote.enums.toTipo
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class TipoConverter : AttributeConverter<Tipo?, String?> {
    override fun convertToDatabaseColumn(tipo: Tipo?): String? {
        return tipo?.id
    }

    override fun convertToEntityAttribute(id: String?): Tipo? {
        return id?.toTipo()
    }
}