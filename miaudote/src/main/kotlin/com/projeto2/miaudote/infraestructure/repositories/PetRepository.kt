package com.projeto2.miaudote.infraestructure.repositories;

import com.projeto2.miaudote.domain.entities.Pet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PetRepository : JpaRepository<Pet, Long>{
    fun findByIdUsuario(@Param("idUsuario") idUsuario: Long): List<Pet>?

    @Query(
        """
        select pet from Pet pet 
        where pet.idUsuario != :idUsuario
        and pet.id not in (
            select adocao.petId from Adocao adocao
        )
        """
    )
    fun findPetsOthersUsuarios(@Param("idUsuario") idUsuario: Long): List<Pet>?

    @Query(
        """
        select pet from Pet pet 
        where pet.id not in (
            select adocao.petId from Adocao adocao
        )"""
    )
    fun findPetsNaoAdotados(): List<Pet>?

    @Query(
        """
        select pet from Pet pet 
        where pet.id in (
            select adocao.petId from Adocao adocao
        )"""
    )
    fun findPetsAdotados(): List<Pet>?

    @Query(
        """
        select pet from Pet pet 
        where pet.id in (
            select adocao.petId from Adocao adocao where adocao.solicitacaoId in (
                select sa.id from SolicitacaoAdocao sa where usuarioAdotante = :idUsuario
            )
        )"""
    )
    fun findPetsAdotadosUsuario(@Param("idUsuario") id: Long): List<Pet>?
}