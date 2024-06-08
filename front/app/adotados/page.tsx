 "use client"
import { Posts } from "../components/Posts";
import { PetService } from "@/data/PetService";
import { PetPost } from "@/domain/Pet"; 
import { useState, useEffect } from "react";

export default function Page() { 
  const petService = new PetService(); 

  const [pets, setPets] = useState<PetPost[]>();

  useEffect(() => {
    petService.obterPetsAdotados()
      .then(res => {
        setPets(res.data)
      }).catch(error => {
        console.log(error)
      });
  }, []);


  return (
    <>
      {/* 
                <CardsReceptacle />
                <ButtonsReceptacle /> 
            */}
      <div className='antialiased bg-theme-bg bg-center bg-cover bg-no-repeat min-h-[2000px] items-center flex flex-col'>
        <Posts animais={pets} inAdotados={true}/>
      </div>
    </>
  )
}
