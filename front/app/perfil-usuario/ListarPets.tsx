'use client'

import { PetPost } from "@/domain/Pet"; 
import { Ref, useRef, useState } from "react";

export default function ListarPets({ petsIn }) {
  const [pets, setPets] = useState<PetPost[]>(petsIn)
  return (
    <>
      <div>
        <h2>Seus pets</h2>
         
      </div>
    </>
  )
}