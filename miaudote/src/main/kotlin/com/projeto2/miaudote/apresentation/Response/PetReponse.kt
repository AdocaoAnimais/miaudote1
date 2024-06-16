package com.projeto2.miaudote.apresentation.Response

import com.projeto2.miaudote.domain.enums.Castrado
import com.projeto2.miaudote.domain.enums.Porte
import com.projeto2.miaudote.domain.enums.Sexo

data class PetPost(
    val id: Long,
    val nome: String,
    val castrado: Castrado?,
    val sexo: Sexo,
    val porte: Porte?,
    val descricao: String?,
    val imageData: ByteArray?,
    val status: Any?,
)