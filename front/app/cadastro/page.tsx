import Link from "next/link";
import Image from 'next/image';
import backIcon from "../../public/back.svg";
import Form from "./Form";

export default async function Page() {
    return (
        <>
            <div className='antialiased bg-theme-bg text-theme-text bg-center bg-cover bg-no-repeat min-h-[2000px] items-center flex flex-col py-16'>
                <div className="flex flex-wrap gap-32">


                    <div className="">
                        <div className="justify-center items-center content-center text-center">
                            <div className="relative w-[200px] h-[200px] overflow-hidden rounded-xl">
                                <Image
                                    src={"/logo.png"}
                                    alt="Minha imagem"
                                    fill
                                    style={{ objectFit: 'contain' }} // cover
                                />
                                {/* <img src={produto.image} alt="Coffee" /> */}
                            </div>
                        </div>
                        <div>
                            <h1 className="py-4">Cadastro</h1>
                            <p className="text-[#737380]">Faça seu cadastro!</p>
                            <p className="text-[#FFF]">Ao realizar o cadastro enviaremos um email para confirmação de cadastro!</p>
                        </div>
                        <div className='w-full gap-2 flex p-12 text-left pt-32 pb-2 px-6'>
                            <Image
                                priority
                                src={backIcon}
                                alt="Follow us on Twitter"
                            />
                            <Link href={"/login"} className=" text-theme-text hover:text-theme-texthighlight">
                                Voltar para login
                            </Link>
                        </div>
                    </div>

                    <div className="text-theme-text2">
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