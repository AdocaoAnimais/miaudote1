"use client";

import { useRef } from "react";
import { useRouter } from "next/navigation";
import Button_Cancel from "./Buttons/Button_Cancel";
import Button_YellowTarja from "./Buttons/Button_YellowTarja";
import { AuthenticationService } from "@/data/AuthenticationService";
import { PetService } from "@/data/PetService";
import { AxiosError } from "axios";

export default function Form({}) {
  const authService = new AuthenticationService();
  const service = new PetService();
  const ref = useRef(null);
  const router = useRouter();

  init();
  async function init(){
    try {
      const response = await authService.logged();
      if(!response){
        console.log("Usuário sem autenticação, direcionando para login");
        router.push("/login");
      }
    } catch(e) {
      console.log(e)
    }
  }

  const handleSubmit = async (event) => {
    event.preventDefault();
 
    const nome = ref.current.nome.value;
    const tipo = ref.current.tipo.value;
    const sexo = ref.current.sexo.value;
    const porte = ref.current.porte.value;
    const idade = parseInt(ref.current.idade.value);
    const descricao = ref.current.descricao.value;
 
    if (idade < 0) {
      alert("A idade não pode ser negativa");
      return;
    }

    try {
      await service.createPet(
        nome,
        idade,
        sexo,
        porte,
        tipo,
        "N",
        descricao,
      )
      router.push("")
    } catch(e) {
      if(e instanceof AxiosError && e.response.status == 400) {
        console.log(e.response)
      }
    };
  };

  return (
    <>
      <form
        ref={ref}
        onSubmit={handleSubmit}
        className="max-w-lg mx-auto grid grid-cols-1 md:grid-cols-2 gap-4"
      >
        <div className="mb-4 md:col-span-2">
          <input
            id="nome"
            name="nome"
            type="text"
            className="bg-[#111333] border mt-1 p-3 block w-full rounded-md border-[#f2a812] focus:border-[#f2a812] focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="Nome do animal"
            required
          />
        </div>

        <div className="mb-4">
          <label htmlFor="tipo" className="block text-sm font-medium">
            Tipo:
          </label>
          <select
            id="tipo"
            name="tipo"
            className="bg-[#111333] border mt-1 p-3 block w-full rounded-md border-[#f2a812] focus:border-[#f2a812] focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            required
          >
            <option value="C">Cachorro</option>
            <option value="G">Gato</option>
          </select>
        </div>

        <div className="mb-4">
          <label htmlFor="sexo" className="block text-sm font-medium">
            Sexo:
          </label>
          <select
            id="sexo"
            name="sexo"
            className="bg-[#111333] border mt-1 p-3 block w-full rounded-md border-[#f2a812] focus:border-[#f2a812] focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            required
          >
            <option value="M">Macho</option>
            <option value="F">Fêmea</option>
          </select>
        </div>

        <div className="mb-4">
          <label htmlFor="porte" className="block text-sm font-medium">
            Porte:
          </label>
          <select
            id="porte"
            name="porte"
            className="bg-[#111333] border mt-1 p-3 block w-full rounded-md border-[#f2a812] focus:border-[#f2a812] focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            required
          >
            <option value="P">Pequeno</option>
            <option value="M">Médio</option>
            <option value="G">Grande</option>
          </select>
        </div>

        <div className="mb-4">
          <label htmlFor="idade" className="block text-sm font-medium">
            Idade:
          </label>
          <input
            id="idade"
            name="idade"
            type="number"
            className="bg-[#111333] border mt-1 p-3 block w-full rounded-md border-[#f2a812] focus:border-[#f2a812] focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="Idade"
            required
          />
        </div>

        <div className="mb-4 md:col-span-2">
          <textarea
            id="descricao"
            name="descricao"
            rows="4"
            className="bg-[#111333] border mt-1 p-3 block w-full rounded-md border-[#f2a812] focus:border-[#f2a812] focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="Descrição"
            required
          ></textarea>
        </div>

        <div className="flex justify-between md:col-span-2">
          <Button_Cancel texto="Cancelar" />
          <Button_YellowTarja texto="Cadastrar" />
        </div>
      </form>
    </>
  );
}