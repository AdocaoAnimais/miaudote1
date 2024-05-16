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

    @Column(name = "email", unique = true, nullable = false)
    val email: String,

    @Column(name = "senha", nullable = false)
    val senha: String,

    @Column(name = "endereco_id")
    val endereco: Long?,

    @Column(name = "contato")
    val contato: String?,

    @Column(name = "perfil_acesso")
    val perfilAcesso: String? = "A",

    @Column(name = "descricao")
    val descricao: String?,

    @Column(name = "email_verificado")
    val emailVerificado: Boolean?,
)