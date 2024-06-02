'use client'

import { useEffect, useState } from "react";
import api from "@/app/App";
import { Posts } from "../Posts";

export default function Card() {
  const [pets, setUser] = useState();
  useEffect(() => {
    api
      .get("http://200.132.38.218:8004/api/pet/obter-pets")
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
