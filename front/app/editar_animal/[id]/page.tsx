"use client";

import { useRef, useState, useEffect } from "react";
import { useParams, useRouter, useSearchParams } from "next/navigation";
import { AuthenticationService } from "@/data/AuthenticationService";
import { PetService } from "@/data/PetService";
import { AxiosError } from "axios";
import Alert from '@mui/material/Alert';
import Stack from '@mui/material/Stack';
import Button_YellowTarja from "@/app/components/Buttons/Button_YellowTarja";
import LinkButton_YellowTarja from "@/app/components/Buttons/LinkButton_YellowTarja";
import Link from "next/link";
import { PetPost } from "@/domain/Pet";
import { Problem } from "@/app/App";

export default function Form({ }) {
    const authService = new AuthenticationService();
    const service = new PetService();
    const ref = useRef(null);
    const router = useRouter();
    const params = useParams()
    const [imagem, setImagem] = useState<Blob | null>(null);
    const [tamImg, setTamImg] = useState(0);
    const [error, setError] = useState<string | null>(null);
    const [sucesso, setSucesso] = useState<string | null>(null);

    const petId: string = params.id
    const [nome, setNome] = useState("");
    const [tipo, setTipo] = useState("");
    const [castrado, setCastrado] = useState("");
    const [sexo, setSexo] = useState("");
    const [porte, setPorte] = useState("");
    const [idade, setIdade] = useState(0);
    const [descricao, setDescricao] = useState(""); 
    useEffect(() => {
        init();
    }, []);

    async function init() {
        try {
            const response: PetPost = await service.obterPorId(petId)
            if (response) {
                setNome(response.nome)
                setTipo(response.tipo.id)
                setSexo(response.sexo.id)
                setPorte(response.porte.id)
                setCastrado(response.castrado.id)
                setIdade(response.idade)
                setDescricao(response.descricao)
                if(response.imageData){
                    document.getElementById("preview_img").src = "data:image/png;base64," + response.imageData;
                    setTamImg(150) 
                }
            }
        } catch (e) {
            console.log(e)
            if (e.response.status == 401) router.push("/login")
        }
    }

    const handleSubmit = async (event) => {
        event.preventDefault();

        setError(null); // Reset error state before attempting login

        const nome = ref.current.nome.value;
        const tipo = ref.current.tipo.value;
        const castrado = ref.current.castrado.value;
        const sexo = ref.current.sexo.value;
        const porte = ref.current.porte.value;
        const idade = parseInt(ref.current.idade.value);
        const descricao = ref.current.descricao.value;

        if (idade < 0) {
            alert("A idade não pode ser negativa");
            return;
        }

        try {
            const response = await service.atualizar(
                nome,
                idade,
                sexo,
                porte,
                tipo,
                castrado,
                descricao,
                petId
            )
            setSucesso("Pet atualizado com sucesso!");
            if (imagem) {
                await service.uploadImagemPet(imagem, response.data.id);
            }
            
            setTimeout(() => router.push("/usuario"), 3000) 
        } catch (e: any) { 
            if (e instanceof Problem && e.status == 400) {
                console.log(e)
            }
            setError(e.detail || "Erro desconhecido!");
        };
    };

    const loadFile = function (event) {
        let input = event.target;
        let file = input.files[0];
        setImagem(file);
        setTamImg(150)
        let type = file.type;
        let output = document.getElementById('preview_img');
        output.src = URL.createObjectURL(event.target.files[0]);
        output.onload = function () {
            URL.revokeObjectURL(output.src) // free memory
        }
    };

    return (
        <>
            <div className="text-white">
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
                            value={nome}
                            onChange={(e) => setNome(e.target.value)}
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
                            value={tipo}
                            onChange={(e) => setTipo(e.target.value)}
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
                            value={sexo}
                            onChange={(e) => setSexo(e.target.value)}
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
                            value={porte}
                            onChange={(e) => setPorte(e.target.value)}
                            className="bg-[#111333] border mt-1 p-3 block w-full rounded-md border-[#f2a812] focus:border-[#f2a812] focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
                            required
                        >
                            <option value="P">Pequeno</option>
                            <option value="M">Médio</option>
                            <option value="G">Grande</option>
                        </select>
                    </div>

                    <div className="mb-5">
                        <label htmlFor="castrado" className="block text-sm font-medium">
                            Castrado:
                        </label>
                        <select
                            id="castrado"
                            name="castrado"
                            value={castrado}
                            onChange={(e) => setCastrado(e.target.value)}
                            className="bg-[#111333] border mt-1 p-3 block w-full rounded-md border-[#f2a812] focus:border-[#f2a812] focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
                            required
                        >
                            <option value="N">Não castrado</option>
                            <option value="C">Castrado</option>
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
                            value={idade}
                            onChange={(e) => setIdade(parseInt(e.target.value))}
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
                            value={descricao}
                            onChange={(e) => setDescricao(e.target.value)}
                            className="bg-[#111333] border mt-1 p-3 block w-full rounded-md border-[#f2a812] focus:border-[#f2a812] focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
                            placeholder="Descrição"
                        ></textarea>
                    </div>
                    {sucesso && (
                        <Stack sx={{ width: '100%' }} spacing={2}>
                            <Alert severity="success">{sucesso}</Alert>
                        </Stack>
                    )}
                    {error && (
                        <Stack sx={{ width: '100%' }} spacing={2}>
                            <Alert severity="error">{error}</Alert>
                        </Stack>
                    )}
                    <div className=" md:col-span-2">
                        <label className="block">
                            <span className="sr-only">Escolha a foto do perfil</span>
                            <input type="file" onChange={loadFile} className="block w-full text-sm text-slate-500
                                file:mr-4 file:py-2 file:px-4
                                file:rounded-full file:border-0
                                file:text-sm file:font-semibold
                                file:bg-violet-50 file:text-violet-700
                                hover:file:bg-violet-100
                            "/>
                        </label>
                        <div className="shrink-0 my-5">
                            <img id='preview_img' className={`h-${tamImg} w-${tamImg} object-cover`}
                                style={{ height: tamImg }}
                                src="/sem-imagem.png"
                                alt="Imgem pet" />
                        </div>
                        <div className="flex justify-between md:col-span-2">
                            <Link href="/usuario">
                                <LinkButton_YellowTarja texto={"Cancelar"} />
                            </Link>

                            <Button_YellowTarja texto="Atualizar" />
                        </div>
                    </div>
                </form>
            </div>
        </>
    );
}
