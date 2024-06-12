'use client'
import { IMaskInput } from 'react-imask';
import { useRef, useState } from "react";
import { useRouter } from "next/navigation";
import { AuthenticationService } from "@/data/AuthenticationService";
import { UsuarioService } from "@/data/UsuarioService";
import Alert from '@mui/material/Alert';
import Stack from '@mui/material/Stack';

export default function Form({ }) {
  const authService = new AuthenticationService();
  const service = new UsuarioService();
  const router = useRouter();

  const [error, setError] = useState<string | null>(null);

  const ref = useRef(null);
  init();
  async function init() {
    try {
      const response = await authService.logged();
      if (response) {
        console.log("Usuário logado, redirecionando para tela inicial.");
        router.push("/");
      }
    } catch (e) {
      console.log(e)
    }
  }
  async function cadastrar(event) {
    event.preventDefault();

    // Obtendo os valores dos campos
    console.log(ref.current)
    const nome = ref.current.nome.value;
    const sobrenome = ref.current.sobrenome.value;
    const username = ref.current.username.value;
    const email = ref.current.email.value;
    const senha = ref.current.senha.value;
    const cpf = ref.current.cpf.value;
    const endereco = ref.current.endereco.value;
    const contato = ref.current.contato.value;

    try {
      await service.cadastrar(
        nome,
        sobrenome,
        username,
        email,
        senha,
        cpf,
        contato,
        endereco,
      );
      router.refresh();
    } catch (e) {
      console.log(e)
      console.log("Erro ao efetuar login: ", e.response?.data?.detail);
      setError(e.response?.data?.detail || "Erro desconhecido ao efetuar login");
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
            htmlFor="nome"
          />
        </div>
        <div className="md:col-span-2">
          <input
            id="sobrenome"
            name="sobrenome"
            type="text"
            className="bg-theme-inputbg border mt-1 p-3 block w-full rounded-md border-theme-border focus:border-theme-border focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="Sobrenome"
            required
            htmlFor="sobrenome"
          />
        </div>
        <div className="md:col-span-2">
          <IMaskInput
            mask="000.000.000-00"
            id="cpf"
            name="cpf"
            type="text"
            className="bg-theme-inputbg border mt-1 p-3 block w-full rounded-md border-theme-border focus:border-theme-border focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="CPF"
            required  
            htmlFor="cpf"
          />
        </div>
        <div className="md:col-span-2">
          <input
            id="username"
            name="username"
            type="text"
            className="bg-theme-inputbg border mt-1 p-3 block w-full rounded-md border-theme-border focus:border-theme-border focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="Username"
            required
            htmlFor="username"
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
            htmlFor="email"
          />
        </div>
        <div className="md:col-span-2">
          <input
            id="senha"
            name="senha"
            type="password"
            className="bg-theme-inputbg border mt-1 p-3 block w-full rounded-md border-theme-border focus:border-theme-border focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="Senha"
            required
            htmlFor="senha"
          />
        </div>
        <div className="">
          <IMaskInput
            mask="00000-000"
            id="endereco"
            name="endereco"
            type="text"
            className="bg-theme-inputbg border mt-1 p-3 block w-full rounded-md border-theme-border focus:border-theme-border focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="CEP"
            htmlFor="endereco"
          />
        </div>
        <div className="">
          <IMaskInput
            mask="(00) 0 00000000"
            id="contato"
            name="contato"
            type="text"
            className="bg-theme-inputbg border mt-1 p-3 block w-full rounded-md border-theme-border focus:border-theme-border focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            placeholder="Celular"
            htmlFor="contato"
          />
        </div>

        <div className="flex justify-center md:col-span-2 pt-8">
          <button
            onClick={cadastrar}
            className="py-4 w-full bg-theme-button1 text-theme-text rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300"
          >
            Cadastrar
          </button>
        </div>

      </form>
      {error && (
        <Stack sx={{ width: '100%' }} spacing={2}>
          <Alert severity="error">{error}</Alert>
        </Stack>
      )}
    </>
  );
}
