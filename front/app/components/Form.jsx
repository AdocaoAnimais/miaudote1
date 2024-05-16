'use client'

import { useEffect, useRef, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import { createPet } from "../services/PetService"

export default function Form({ }) {
    const ref = useRef(null);

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
        )

        // console.log('Nome:', nome);
        // console.log('Tipo:', tipo);
        // console.log('Sexo:', sexo);
        // console.log('Porte:', porte);
        // console.log('Idade:', idade);
        // console.log('Descrição:', descricao);
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
                    <label htmlFor="nome" className="block text-sm font-medium text-gray-700">
                        Nome:
                    </label>
                    <input
                        id="nome"
                        name="nome"
                        type="text"
                        className="mt-1 p-3 block w-full rounded-md border-gray-300 focus:border-indigo-500 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
                        placeholder="Digite o nome do animal"
                        required
                    />
                </div>

                <div className="mb-4">
                    <label htmlFor="tipo" className="block text-sm font-medium text-gray-700">
                        Tipo:
                    </label>
                    <select
                        id="tipo"
                        name="tipo"
                        className="mt-1 p-3 block w-full rounded-md border-gray-300 focus:border-indigo-500 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
                        required
                    >
                        <option value="C">Cachorro</option>
                        <option value="G">Gato</option>
                    </select>
                </div>

                <div className="mb-4">
                    <label htmlFor="sexo" className="block text-sm font-medium text-gray-700">
                        Sexo:
                    </label>
                    <select
                        id="sexo"
                        name="sexo"
                        className="mt-1 p-3 block w-full rounded-md border-gray-300 focus:border-indigo-500 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
                        required
                    >
                        <option value="M">Macho</option>
                        <option value="F">Fêmea</option>
                    </select>
                </div>

                <div className="mb-4">
                    <label htmlFor="porte" className="block text-sm font-medium text-gray-700">
                        Porte:
                    </label>
                    <select
                        id="porte"
                        name="porte"
                        className="mt-1 p-3 block w-full rounded-md border-gray-300 focus:border-indigo-500 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
                        required
                    >
                        <option value="P">Pequeno</option>
                        <option value="M">Médio</option>
                        <option value="G">Grande</option>
                    </select>
                </div>

                <div className="mb-4">
                    <label htmlFor="idade" className="block text-sm font-medium text-gray-700">
                        Idade:
                    </label>
                    <input
                        id="idade"
                        name="idade"
                        type="number"
                        className="mt-1 p-3 block w-full rounded-md border-gray-300 focus:border-indigo-500 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
                        placeholder="Digite a idade do animal"
                        required
                    />
                </div>

                <div className="mb-4 md:col-span-2">
                    <label htmlFor="descricao" className="block text-sm font-medium text-gray-700">
                        Descrição:
                    </label>
                    <textarea
                        id="descricao"
                        name="descricao"
                        rows="4"
                        className="mt-1 p-3 block w-full rounded-md border-gray-300 focus:border-indigo-500 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
                        placeholder="Descreva o animal"
                        required
                    ></textarea>
                </div>

                <div className="flex justify-end md:col-span-2">
                    <button
                        type="button"
                        className="py-2 px-4 bg-gray-200 text-gray-800 rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300"
                    >
                        Cancelar
                    </button>
                    <button
                        type="submit"
                        className="ml-2 py-2 px-6 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:bg-indigo-700"
                    >
                        Cadastrar
                    </button>
                </div>
            </form>
        </>
    )
}
