package com.projeto2.miaudote.services

import com.projeto2.miaudote.entities.Usuario
import com.projeto2.miaudote.repositories.UsuarioRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UsuarioService (
    val repository: UsuarioRepository
) {
    fun obterTodos(): List<Usuario> {
        return repository.findAll()
    }

    fun obterPorId(id: Long): Usuario? {
        return repository.findById(id).getOrNull()
    }

    fun criar(usuario: Usuario): Usuario {
        return repository.save(usuario)
    }

    fun deletar(id: Long) {

        return repository.deleteById(id)
    }

    fun atualizar(usuario: Usuario): Usuario {
        return repository.save(usuario)
    }
}