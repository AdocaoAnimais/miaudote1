package com.projeto2.miaudote.repositories

import com.projeto2.miaudote.entities.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


interface UsuarioRepository : JpaRepository<Usuario, Long > {
    fun findByUsername(@Param("username") username: String?): Usuario?
}