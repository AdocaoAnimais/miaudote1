"use client";

import { useEffect, useRef, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import { createPet } from "../services/PetService";
import { useRouter } from "next/navigation";

export default function Form({ }) {
    const ref = useRef(null);
    const router = useRouter()

    const handleSubmit = (event) => {
        event.preventDefault(); 
        // Obtendo os valores dos campos
        const nome = ref.current.nome.value;
        const tipo = ref.current.tipo.value;
        const sexo = ref.current.sexo.value;
        const porte = ref.current.porte.value;
        const idade = parseInt(ref.current.idade.value);
        const descricao = ref.current.descricao.value;

        // Validando a idade
        if (idade < 0) {
            alert('A idade não pode ser negativa');
            return;
        }


        createPet(
            nome,
            idade.toString(),
            sexo,
            porte,
            tipo,
            "N",
            descricao,
        ).then( res => {
            router.push("/")
        });
    };

    return (
        <>
            {/* <form action="" ref={ref} onSubmit={handleSubmit}>

                <label htmlFor="">Nome: </label>
                <input name="nome" type="text" />

                <label htmlFor="">E-mail: </label>
                <input name="email" type="email" />

                <label htmlFor="">Telefone: </label>
                <input name="fone" type="number" />

                <label>Data de Nascimento</label>
                <input name="data_nascimento" type="date" />

                <button type="submit">SALVAR</button>
            </form> */}


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
            <option value="cachorro">Cachorro</option>
            <option value="gato">Gato</option>
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
            <option value="macho">Macho</option>
            <option value="femea">Fêmea</option>
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
            <option value="pequeno">Pequeno</option>
            <option value="medio">Médio</option>
            <option value="grande">Grande</option>
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
