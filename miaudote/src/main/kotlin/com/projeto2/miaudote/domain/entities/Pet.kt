package com.projeto2.miaudote.domain.entities

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.projeto2.miaudote.application.problems.Problem
import com.projeto2.miaudote.domain.enums.Castrado
import com.projeto2.miaudote.domain.enums.Porte
import com.projeto2.miaudote.domain.enums.Sexo
import com.projeto2.miaudote.domain.enums.Tipo
import com.projeto2.miaudote.domain.enums.converters.CastradoConverter
import com.projeto2.miaudote.domain.enums.converters.PorteConverter
import com.projeto2.miaudote.domain.enums.converters.SexoConverter
import com.projeto2.miaudote.domain.enums.converters.TipoConverter
import com.projeto2.miaudote.domain.serialization.BlobDeserializer
import com.projeto2.miaudote.domain.serialization.BlobSerializer
import jakarta.persistence.*
import org.springframework.http.HttpStatus
import java.net.URI
import java.sql.Blob
import java.time.LocalDateTime

@Entity
@Table(name = "pet")
data class Pet (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    val id: Long?,

    @Column(name = "nome", nullable = true)
    val nome: String,

    @Column(name = "idade", nullable = true)
    val idade: Int?,

    @Convert(converter = PorteConverter::class)
    @Column(name = "porte", nullable = true)
    val porte: Porte?,

    @Convert(converter = SexoConverter::class)
    @Column(name = "sexo", nullable = false)
    val sexo: Sexo,

    @Convert(converter = TipoConverter::class)
    @Column(name = "tipo", nullable = false)
    val tipo: Tipo?,

    @Convert(converter = CastradoConverter::class)
    @Column(name = "castrado", nullable = true)
    val castrado: Castrado?,

    @Column(name = "descricao", length = 240, nullable = true)
    val descricao: String?,

    @Column(name = "usuario_cadastro_id", nullable = false)
    val idUsuario: Long,

    @Column(name = "data_cadastro", nullable = false)
    val dataCadastro: LocalDateTime? = LocalDateTime.now(),

    @JsonSerialize(using = BlobSerializer::class)
    @JsonDeserialize(using = BlobDeserializer::class)
    @Column(name = "imageData")
    val imageData: Blob?,
)

fun Pet?.toProblem(): Result<Pet> {
    if(this != null) return Result.success(this)
    return Result.failure(
        Problem(
            title = "Pet não encontrado",
            detail = "O pet com id informado não esta cadastrado",
            type = URI("/obter-pet-por-id"),
            status = HttpStatus.BAD_REQUEST,
            extra = null
        )
    )
}