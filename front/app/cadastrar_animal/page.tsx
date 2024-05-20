import Link from "next/link";
import Form from "../components/Form";
import Image from 'next/image';
import backIcon from "../../public/back.svg";

export default async function Page() {
    return (
        <>
            <div className='antialiased bg-[#0b132d] text-white bg-center bg-cover bg-no-repeat min-h-[2000px] items-center flex flex-col py-16'>
                <div className="flex flex-wrap gap-32">


                    <div className="">
                        <div className="img w-[300px] h-[300px]">

                        </div>
                        <div>
                            <h1 className="py-4">Cadastrar novo animal</h1>
                            <p className="text-[#737380]">Descreva o caso detalhadamente para encontrar um herói para resolver isso.</p>
                        </div>
                        <div className='w-full gap-2 flex p-12 text-left pt-32 pb-2 px-6'>
                            <Image
                                priority
                                src={backIcon}
                                alt="Follow us on Twitter"
                            />
                            <Link href={"/"} className=" text-white hover:text-[#f2a812]">
                                Voltar para home
                            </Link>
                        </div>
                    </div>

                    <div className="text-[#737380]">
                        <Form />
                        {/* Create */}
                        {/* getUsers={getUsers} onEdit={onEdit} setOnEdit={setOnEdit} */}
                        {/* focus:outline-none focus:bg-indigo-500 */}
                    </div>


                </div>
            </div>
        </>
    )
}