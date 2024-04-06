'use client'

import { useEffect, useState } from "react";
import axios from "axios";
import RowBanco from "./RowBanco";
import Form from "./Form";
import { toast } from "react-toastify";


export default function ListBanco() {

    const [users, setUsers] = useState([]);
    const [onEdit, setOnEdit] = useState(null);


    const getUsers = async () => {
        try {
            const res = await axios.get("https://dogs-back-gamma.vercel.app/");
            setUsers(res.data.sort((a: any, b: any) => (a.nome > b.nome ? 1 : -1)));
        } catch (error:any) {
            toast.error(error);
        }
    };

    useEffect(() => {
        getUsers();
    }, [setUsers]);

    const newTab = async () => {
        try {
            const res = await axios.get("https://dogs-back-gamma.vercel.app/");
            setUsers(res.data.sort((a: any, b: any) => (a.nome > b.nome ? 1 : -1)));
        } catch (error:any) {
            toast.error(error);
        }
    };

    useEffect(() => {
        getUsers();
    }, [setUsers]);

    return (
        <>
            {/* CRUD: Create, Read, Update, Delete */}

            {/* READ */}
            {users && users.length > 0 ? (
                users.map((user, index) => <RowBanco key={index} user={user} users={users} setUsers={setUsers} setOnEdit={setOnEdit} />)
            ) : (
                <p>Nenhum produto disponível.</p>
            )}
            
            {/* Create */}
            <Form getUsers={getUsers} onEdit={onEdit} setOnEdit={setOnEdit}/>

            <button onClick={() => newTab()}>NewTab</button>
        </>
    )
}
