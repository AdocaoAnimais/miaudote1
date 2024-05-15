package com.projeto2.miaudote.repositories;

import com.projeto2.miaudote.entities.Pet
import org.springframework.data.jpa.repository.JpaRepository

interface PetRepository : JpaRepository<Pet, Long> {}