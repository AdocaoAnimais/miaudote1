"use client";

import { useRef, useState } from "react";
import { UsuarioService } from "@/data/UsuarioService";
import { Usuario } from "@/domain/Usuario";
import { Alert, Stack } from "@mui/material";
import { AxiosError } from "axios";
import { useRouter } from "next/navigation";
import { IMaskInput } from "react-imask";

export default function Form({ usuario }: { usuario: Usuario }) {
  const service = new UsuarioService();
  const router = useRouter();
  const [error, setError] = useState<string | null>(null);

  const [nome, setNome] = useState(usuario.nome)
  const [sobrenome, setsobrenome] = useState(usuario.sobrenome)
  const [email, setemail] = useState(usuario.email)
  const [username, setusername] = useState(usuario.username)
  const [contato, setcontato] = useState(usuario.contato)
  const [cpf, setcpf] = useState(usuario.cpf)
  const [endereco, setendereco] = useState(usuario.endereco)
  const [senha, setsenha] = useState("")
  const ref = useRef(null);

  const atualizar = async (event) => {
    setError(null);
    console.log('')
    event.preventDefault();
    
    const nomeOut = nome
    const sobrenomeOut = sobrenome
    const usernameOut = username
    const emailOut = email
    const senhaOut = senha
    const cpfOut = cpf.replaceAll('.','').replaceAll('-','')
    const enderecoOut = endereco.replaceAll('-','')
    const contatoOut = contato.replaceAll(' ','').replaceAll('(','').replaceAll(')','')
    try {
      await service.atualizar(
        nomeOut,
        sobrenomeOut,
        usernameOut,
        emailOut,
        cpfOut,
        contatoOut,
        enderecoOut,
        senhaOut,
      )
      router.push("/usuario");
    } catch (e) {
      if (e instanceof AxiosError && e.response?.status == 400) {
        console.log(e.response)
      }
      setError(e.response?.data?.detail || "Erro desconhecido ao efetuar login");
    }
  };

  return (
    <>
      <form
        ref={ref}
        className="max-w-lg mx-auto grid grid-cols-1 md:grid-cols-2 gap-3"
        onSubmit={atualizar}
      >
        <div className="md:col-span-2">
          <input
            type="text"
            className="bg-theme-inputbg border mt-1 p-3 block w-full rounded-md border-theme-border focus:border-theme-border focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="Seu Nome"
            required
            value={nome}
            onChange={(e) => setNome(e.target.value)}
          />
        </div>
        <div className="md:col-span-2">
          <input
            type="text"
            className="bg-theme-inputbg border mt-1 p-3 block w-full rounded-md border-theme-border focus:border-theme-border focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="Seu Sobrenome"
            required
            value={sobrenome}
            onChange={(e) => setsobrenome(e.target.value)}
          />
        </div>
        <div className="md:col-span-2">
          <IMaskInput
            mask="000.000.000-00"
            id="contato"
            type="text"
            className="bg-theme-inputbg border mt-1 p-3 block w-full rounded-md border-theme-border focus:border-theme-border focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="Seu cpf"
            required
            value={cpf}
            onChange={(e) => setcpf(e.target.value)}
          />
        </div>
        <div className="md:col-span-2">
          <input
            type="text"
            className="bg-theme-inputbg border mt-1 p-3 block w-full rounded-md border-theme-border focus:border-theme-border focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="Seu Username"
            required
            value={username}
            onChange={(e) => setusername(e.target.value)}
          />
        </div>
        <div className="md:col-span-2">
          <input
            type="text"
            className="bg-theme-inputbg border mt-1 p-3 block w-full rounded-md border-theme-border focus:border-theme-border focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="E-mail"
            required
            value={email}
            onChange={(e) => setemail(e.target.value)}
          />
        </div>
        <div className="md:col-span-2">
          <input
            type="password"
            className="bg-theme-inputbg border mt-1 p-3 block w-full rounded-md border-theme-border focus:border-theme-border focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="Senha"
            value={senha}
            onChange={(e) => setsenha(e.target.value)}
          />
        </div>
        <div className="">
          <IMaskInput
            mask="00000-000"
            type="text"
            className="bg-theme-inputbg border mt-1 p-3 block w-full rounded-md border-theme-border focus:border-theme-border focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="CEP"
            value={endereco}
            onChange={(e) => setendereco(e.target.value)}
          />
        </div>
        <div className="">
          <IMaskInput
            mask="(00) 0 00000000"
            id="contato"
            type="text"
            className="bg-theme-inputbg border mt-1 p-3 block w-full rounded-md border-theme-border focus:border-theme-border focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="Celular"
            value={contato}
            onChange={(e) => { setcontato(e.target.value) }}
          />
        </div>
        {error && (
          <Stack sx={{ width: '100%' }} spacing={2}>
            <Alert severity="error">{error}</Alert>
          </Stack>
        )}
        <div className="flex justify-center md:col-span-2 pt-8">
          <button
            type="submit"
            className="py-4 w-full bg-theme-button1 text-theme-text rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300"
          >
            Atualizar
          </button>
        </div>
      </form>
    </>
  );
}
