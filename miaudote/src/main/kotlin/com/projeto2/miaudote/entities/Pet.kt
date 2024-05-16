package com.projeto2.miaudote.entities

import com.projeto2.miaudote.enums.Castrado
import com.projeto2.miaudote.enums.Porte
import com.projeto2.miaudote.enums.Sexo
import com.projeto2.miaudote.enums.Tipo
import com.projeto2.miaudote.enums.converters.CastradoConverter
import com.projeto2.miaudote.enums.converters.PorteConverter
import com.projeto2.miaudote.enums.converters.SexoConverter
import com.projeto2.miaudote.enums.converters.TipoConverter
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "pet")
class Pet (

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
)