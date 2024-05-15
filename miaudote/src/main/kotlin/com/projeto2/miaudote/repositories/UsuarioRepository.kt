package com.projeto2.miaudote.repositories

import com.projeto2.miaudote.entities.Usuario
import org.springframework.data.jpa.repository.JpaRepository

interface UsuarioRepository : JpaRepository<Usuario, Long >