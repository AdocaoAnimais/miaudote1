package com.projeto2.miaudote.infraestructure.repositories

import com.projeto2.miaudote.domain.entities.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


interface UsuarioRepository : JpaRepository<Usuario, Long > {
    fun findByUsername(@Param("username") username: String?): Usuario?
    fun findByEmailOrCpfOrUsername(email: String, cpf:String, username: String): Usuario?
    fun findByCpf(@Param("cpf") cpf: String?): Usuario?
    fun findByEmail(@Param("email") email: String): Usuario?
}