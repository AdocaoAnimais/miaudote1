package com.projeto2.miaudote.entities

import jakarta.persistence.*

@Entity
@Table(name = "usuario")
class Usuario (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id", unique = true)
    val id: Long,

    @Column(name = "nome", nullable = false)
    val nome: String,

    @Column(name = "sobrenome", nullable = false)
    val sobrenome: String,

    @Column(name = "username", unique = true, nullable = false)
    val username: String,

    @Column(name = "cpf", unique = true, nullable = false)
    val cpf: String,

    @Column(name = "EMAIL", unique = true, nullable = false)
    val email: String,

    @Column(name = "SENHA", nullable = false)
    val senha: String,

    @Column(name = "ENDERECO_ID")
    val endereco: Long?,

    @Column(name = "CONTATO")
    val contato: String?,

    @Column(name = "PERFIL_ACESSO")
    val perfilAcesso: String? = "A",

    @Column(name = "DESCRICAO")
    val descricao: String?,

    @Column(name = "EMAIL_VERIFICADO")
    val emailVerificado: Boolean?,
)