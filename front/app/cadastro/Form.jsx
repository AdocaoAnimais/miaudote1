"use client";

import { useEffect, useRef, useState } from "react";

export default function Form({}) {
  //getUsers, onEdit, setOnEdit

  // const ref = useRef();

  // useEffect(() => {
  //     if (onEdit) {
  //         const user = ref.current;

  //         user.nome.value = onEdit.nome;
  //         user.email.value = onEdit.email;
  //         user.fone.value = onEdit.fone;
  //         user.data_nascimento.value = onEdit.data_nascimento;
  //     }
  // }, [onEdit]);

  // const handleSubmit = async (e) => {
  //     e.preventDefault();

  //     const user = ref.current;

  //     if (
  //         !user.nome.value ||
  //         !user.email.value ||
  //         !user.fone.value ||
  //         !user.data_nascimento.value
  //     ) {
  //         return toast.warn("Preencha todos os campos!");
  //     }

  //     if (onEdit) {
  //         await axios
  //             .put("https://dogs-back-gamma.vercel.app/" + onEdit.id, {
  //                 nome: user.nome.value,
  //                 email: user.email.value,
  //                 fone: user.fone.value,
  //                 data_nascimento: user.data_nascimento.value,
  //             })
  //             .then(({ data }) => toast.success(data))
  //             .catch(({ data }) => toast.error(data));
  //     } else {
  //         await axios
  //             .post("https://dogs-back-gamma.vercel.app", {
  //                 nome: user.nome.value,
  //                 email: user.email.value,
  //                 fone: user.fone.value,
  //                 data_nascimento: user.data_nascimento.value,
  //             })
  //             .then(({ data }) => toast.success(data))
  //             .catch(({ data }) => toast.error(data));
  //     }

  //     user.nome.value = "";
  //     user.email.value = "";
  //     user.fone.value = "";
  //     user.data_nascimento.value = "";

  //     setOnEdit(null);
  //     getUsers();
  // };

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
      alert("A idade não pode ser negativa");
      return;
    }

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
        className="max-w-lg mx-auto grid grid-cols-1 md:grid-cols-2 gap-3"
      >
        <div className="md:col-span-2">
          <input
            id="nome"
            name="nome"
            type="text"
            className="bg-[#111333] border mt-1 p-3 block w-full rounded-md border-[#f2a812] focus:border-[#f2a812] focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="Seu Nome"
            required
          />
        </div>
        <div className="md:col-span-2">
          <input
            id="nome"
            name="nome"
            type="text"
            className="bg-[#111333] border mt-1 p-3 block w-full rounded-md border-[#f2a812] focus:border-[#f2a812] focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="E-mail"
            required
          />
        </div>
        <div className="md:col-span-2">
          <input
            id="nome"
            name="nome"
            type="text"
            className="bg-[#111333] border mt-1 p-3 block w-full rounded-md border-[#f2a812] focus:border-[#f2a812] focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="Celular"
            required
          />
        </div>

        <div className="">
          <input
            id="idade"
            name="idade"
            type="text"
            className="bg-[#111333] border mt-1 p-3 block w-full rounded-md border-[#f2a812] focus:border-[#f2a812] focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="Cidade"
            required
          />
        </div>

        <div className="">
          <input
            id="idade"
            name="idade"
            type="text"
            className="bg-[#111333] border mt-1 p-3 block w-full rounded-md border-[#f2a812] focus:border-[#f2a812] focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="UF"
            required
          />
        </div>

        

        <div className="flex justify-center md:col-span-2 pt-8">
          <button
            type="submit"
            className="py-4 w-full bg-[#f2a812] text-white rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300"
          >
            Cadastrar
          </button>
        </div>
      </form>
    </>
  );
}
