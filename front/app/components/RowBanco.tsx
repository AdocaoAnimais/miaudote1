'use client'

import axios from "axios";

// { id: 2, nome: 'Usuário 2', email: 'email2@email.com', fone: '987654321', data_nascimento: '02/02/2000' }

export default function RowBanco({ user, users, setUsers, setOnEdit }: any) {

    const handleEdit = (user: any) => {
        setOnEdit(user);
    };

    const handleDelete = async (id: any) => {
        await axios
            .delete("https://dogs-back-gamma.vercel.app/" + id)
            .then(({ data }) => {
                const newArray = users.filter((user: any) => user.id !== id);

                setUsers(newArray);
                // toast.success(data);
            })
        // .catch(({ data }) => toast.error(data));

        setOnEdit(null);
    };

    return (
        <>
            <div>
                <div>
                    {user.nome}
                </div>

                <button onClick={() => handleDelete(user.id)}>
                    Delete
                </button>

                <button onClick={() => handleEdit(user)}>
                    Edit
                </button>
            </div>
        </>
    )
}
