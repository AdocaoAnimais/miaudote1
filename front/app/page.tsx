import ListBanco from './components/ListBanco';
import Card from './components/Card.tsx';
import Link from 'next/link';

export default async function Page() {
    
    return (
        <>
            <div className='antialiased bg-slate-600 bg-center bg-cover bg-no-repeat min-h-[2000px] items-center flex flex-col'>

                <div className='w-full p-12 text-right'>
                    <Link href={"/cadastrar_animal"} className="ml-2 py-2 px-6 bg-indigo-600 text-white rounded-md hover:bg-indigo-500 focus:outline-none focus:bg-indigo-500">
                        Cadastrar animal
                    </Link>
                </div>

                <div className='w-full p-12 text-right'>
                    <Link href={"/test-axios"} className="ml-2 py-2 px-6 bg-indigo-600 text-white rounded-md hover:bg-indigo-500 focus:outline-none focus:bg-indigo-500">
                        Test Axios
                    </Link>
                </div>


                <Card />




                {/* <ListBanco /> */}
            </div>
        </>
    )
}