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

}