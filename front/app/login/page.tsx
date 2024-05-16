import Form from "./Form";

export default async function Page() {
    return (
        <>
            <div className='antialiased bg-slate-600 bg-center bg-cover bg-no-repeat min-h-[2000px] items-center flex flex-col py-16'>
                 {/* Create */}
                <Form />
                {/* getUsers={getUsers} onEdit={onEdit} setOnEdit={setOnEdit} */}
            </div>
        </>
    )
}