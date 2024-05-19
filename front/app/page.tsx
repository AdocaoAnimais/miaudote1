'use client'
import React, { use } from 'react';
import Card from './components/Card.tsx';
import Link from 'next/link'; 



export default class Page extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: null
        }
    }

    async userLoad() {
        // const username = await obterUsuarioLogado();
        // console.log(username)
        // this.setState({
        //     username
        // })
    }

    componentDidMount(): void {
        this.userLoad()
    }

    render() {
        return (
            <>
                <div className='antialiased bg-slate-600 bg-center bg-cover bg-no-repeat min-h-[2000px] items-center flex flex-col'>
                    <div className='w-full p-12 text-right'>
                        {
                            this.state.username ? (
                                <Link href={"/cadastrar_animal"} className="ml-2 py-2 px-6 bg-indigo-600 text-white rounded-md hover:bg-indigo-500 focus:outline-none focus:bg-indigo-500">
                                    Cadastrar animal
                                </Link>
                    
                            ) : <Link href={"/cadastro"} className="ml-2 py-2 px-6 bg-indigo-600 text-white rounded-md hover:bg-indigo-500 focus:outline-none focus:bg-indigo-500">
                                Cadastre-se
                            </Link>
                        }
                        {
                            this.state.username ? (
                                <button  className="ml-2 py-2 px-6 bg-indigo-600 text-white rounded-md hover:bg-indigo-500 focus:outline-none focus:bg-indigo-500">
                                    {this.state.username}
                                </button>
                    
                            ) : <Link href={"/login"} className="ml-2 py-2 px-6 bg-indigo-600 text-white rounded-md hover:bg-indigo-500 focus:outline-none focus:bg-indigo-500">
                                Login
                            </Link>
                        }
                        
                    </div>
                    <Card />
                </div>
            </>
        )
    }
}