package com.projeto2.miaudote.application.services

import com.projeto2.miaudote.domain.entities.Usuario
import com.projeto2.miaudote.infraestructure.repositories.UsuarioRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UsuarioService(
    val repository: UsuarioRepository
) {

    fun obterPorEmail(email: String): Usuario? {
        return repository.findByEmail(email = email)
    }

    fun obterPorCpf(cpf: String): Usuario? {
        return repository.findByCpf(cpf)
    }

    fun obterUsername(username: String): Usuario? {
        return repository.findByUsername(username)
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