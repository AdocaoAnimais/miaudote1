'use client'

import { useEffect, useState } from "react";
import api, { BASE_URL } from "@/app/App";
import { Posts } from "../Posts";

export default function Card() {
  const [pets, setUser] = useState();
  useEffect(() => {
    api
      .get(`${BASE_URL}/api/pet/obter-pets`)
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
