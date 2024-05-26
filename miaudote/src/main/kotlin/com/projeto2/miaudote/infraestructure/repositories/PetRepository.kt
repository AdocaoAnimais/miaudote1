package com.projeto2.miaudote.infraestructure.repositories;

import com.projeto2.miaudote.domain.entities.Pet
import org.springframework.data.jpa.repository.JpaRepository

interface PetRepository : JpaRepository<Pet, Long>