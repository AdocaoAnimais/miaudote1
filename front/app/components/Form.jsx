'use client'

import { useEffect, useRef, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";

export default function Form({ getUsers, onEdit, setOnEdit }) {

    const ref = useRef();

    useEffect(() => {
        if (onEdit) {
            const user = ref.current;

            user.nome.value = onEdit.nome;
            user.email.value = onEdit.email;
            user.fone.value = onEdit.fone;
            user.data_nascimento.value = onEdit.data_nascimento;
        }
    }, [onEdit]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        const user = ref.current;

        if (
            !user.nome.value ||
            !user.email.value ||
            !user.fone.value ||
            !user.data_nascimento.value
        ) {
            return toast.warn("Preencha todos os campos!");
        }

        if (onEdit) {
            await axios
                .put("https://dogs-back-gamma.vercel.app/" + onEdit.id, {
                    nome: user.nome.value,
                    email: user.email.value,
                    fone: user.fone.value,
                    data_nascimento: user.data_nascimento.value,
                })
                .then(({ data }) => toast.success(data))
                .catch(({ data }) => toast.error(data));
        } else {
            await axios
                .post("https://dogs-back-gamma.vercel.app", {
                    nome: user.nome.value,
                    email: user.email.value,
                    fone: user.fone.value,
                    data_nascimento: user.data_nascimento.value,
                })
                .then(({ data }) => toast.success(data))
                .catch(({ data }) => toast.error(data));
        }

        user.nome.value = "";
        user.email.value = "";
        user.fone.value = "";
        user.data_nascimento.value = "";

        setOnEdit(null);
        getUsers();
    };

    return (
        <>
            <form action="" ref={ref} onSubmit={handleSubmit}>
                <label htmlFor="">Nome: </label>
                <input name="nome" type="text" />

                <label htmlFor="">E-mail: </label>
                <input name="email" type="email" />

                <label htmlFor="">Telefone: </label>
                <input name="fone" type="number" />

                <label>Data de Nascimento</label>
                <input name="data_nascimento" type="date" />

                <button type="submit">SALVAR</button>
            </form>

            
        </>
    )
}
