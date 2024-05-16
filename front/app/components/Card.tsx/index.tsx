'use client'

import { useEffect, useState } from "react";
import { Posts } from "../Posts" 
import { PetPost } from "@/app/services/PetService";
import axios from "axios";
import api from "@/app/App";

export default function Card() {
  const [pets, setUser] = useState(); 
  useEffect(() => {
    api
      .get("http://127.0.0.1:8080/api/pet/")
      .then((response) => setUser(response.data))
      .catch((err) => {
        console.error("ops! ocorreu um erro" + err);
      });
  }, []);  

  
  return (
    <>
      <Posts animais={pets} />
    </>
  )
}
