"use client";

import { useRef, useState } from "react"; 
import { UsuarioService } from "@/data/UsuarioService"; 
import { Usuario } from "@/domain/Usuario";
import { Alert, Stack } from "@mui/material";

export default function Form({usuario}: {usuario: Usuario}) { 
  const service = new UsuarioService(); 
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

  async function atualizar(){
    setError(null);
    const nome = ref.current.nome.value;
    try{
      await service.atualizar(
        nome,
        sobrenome,
        username,
        email,
        cpf, 
        contato,
        endereco,
        senha
      )
      // router.push("/usuario"); 
    } catch(e) {
      // if (e instanceof AxiosError && e.response?.status == 400) {
      //   console.log(e.response)
      // }
      // setError(e.response?.data?.detail || "Erro desconhecido ao efetuar login");
    }
  };

  return (
    <>
      <form 
        ref={ref} 
        className="max-w-lg mx-auto grid grid-cols-1 md:grid-cols-2 gap-3"
      >
        <div className="md:col-span-2">
          <input
            id="nome"
            name="nome"
            type="text"
            className="bg-theme-inputbg border mt-1 p-3 block w-full rounded-md border-theme-border focus:border-theme-border focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="Seu Nome"
            required
            value={nome} 
          />
        </div>
        <div className="md:col-span-2">
          <input
            id="sobrenome"
            name="sobrenome"
            type="text"
            className="bg-theme-inputbg border mt-1 p-3 block w-full rounded-md border-theme-border focus:border-theme-border focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="Seu Sobrenome"
            required
            value={sobrenome}
            onChange={(e) => setsobrenome(e.target.value)}
          />
        </div>
        <div className="md:col-span-2">
          <input
            id="cpf"
            name="cpf"
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
            id="username"
            name="username"
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
            id="email"
            name="email"
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
            id="senha"
            name="senha"
            type="password"
            className="bg-theme-inputbg border mt-1 p-3 block w-full rounded-md border-theme-border focus:border-theme-border focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="Senha" 
            value={senha}
            onChange={(e) => setsenha(e.target.value)}
          />
        </div>
        <div className="">
          <input
            id="endereco"
            name="endereco"
            type="text"
            className="bg-theme-inputbg border mt-1 p-3 block w-full rounded-md border-theme-border focus:border-theme-border focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="CEP"
            value={endereco}
            onChange={(e) => setendereco(e.target.value)}
          />
        </div> 
        <div className="">
          <input
            id="contato"
            name="contato"
            type="text"
            className="bg-theme-inputbg border mt-1 p-3 block w-full rounded-md border-theme-border focus:border-theme-border focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="Celular"
            value={contato}
            onChange={(e) => setcontato(e.target.value)}
          />
        </div>
        {error && (
          <Stack sx={{ width: '100%' }} spacing={2}>
            <Alert severity="error">{error}</Alert>
          </Stack>
        )}
        <div className="flex justify-center md:col-span-2 pt-8">
          <button
            onClick={atualizar}
            className="py-4 w-full bg-theme-button1 text-theme-text rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300"
          >
            Atualizar
          </button>
        </div>
      </form>
    </>
  );
}
