"use client"

import {
    Disclosure,
    DisclosureButton,
    DisclosurePanel,
    Menu,
    MenuButton,
    MenuItem,
    MenuItems,
    Transition,
} from '@headlessui/react'
import Image from 'next/image'
import { Fragment, useState } from 'react'
import { AuthenticationService } from '@/data/AuthenticationService';
import Link from 'next/link';
import { useRouter } from 'next/navigation';

interface NavigationItem {
    name: string
    href: string
    current: boolean
}

const navigation: NavigationItem[] = [
    { name: 'Home', href: '/', current: true },
    // { name: 'Sobre', href: '/sobre', current: false },
    // { name: 'Cadastre-se', href: '/cadastro', current: false },
    // { name: 'Calendar', href: '#', current: false },
]

function classNames(...classes: (string | false | null | undefined)[]): string {
    return classes.filter(Boolean).join(' ')
}

export default function NavBar() {
    const router = useRouter()
    const [logado, setLogado] = useState(false);
    const service = new AuthenticationService();
    
    init();
    async function init() {
        const response = await service.logged();
        setLogado(response)
    }

    function loggout() {
        service.logout();
        setLogado(false)
    }

    async function entrar(){
        await init();
        if(!logado){
            router.push("/login");
        }
    }

    return (
        <Disclosure as="nav" className="bg-gray-900">
            {({ open }: { open: boolean }) => (
                <>
                    <div className="mx-auto max-w-7xl px-2 sm:px-6 lg:px-8">
                        <div className="relative flex h-16 items-center justify-between">
                            <div className="absolute inset-y-0 left-0 flex items-center sm:hidden">
                                {/* Mobile menu button*/}
                                <DisclosureButton className="relative inline-flex items-center justify-center rounded-md p-2 text-gray-400 hover:bg-gray-700 hover:text-white focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white">
                                    <span className="absolute -inset-0.5" />
                                    <span className="sr-only">Open main menu</span>
                                    {open ? (
                                        <span className="block h-6 w-6">X</span> // Placeholder for XMarkIcon
                                    ) : (
                                        <span className="block h-6 w-6">☰</span> // Placeholder for Bars3Icon
                                    )}
                                </DisclosureButton>
                            </div>
                            <div className="flex flex-1 items-center justify-center sm:items-stretch sm:justify-start">
                                <div className="flex flex-shrink-0 items-center">
                                    <Image
                                        className="h-8 w-auto"
                                        src="/logo.png"
                                        alt="Your Company"
                                        width={32}
                                        height={32}
                                    />
                                </div>
                                <div className="hidden sm:ml-6 sm:block">
                                    <div className="flex space-x-4">
                                        {navigation.map((item) => (
                                            <a
                                                key={item.name}
                                                href={item.href}
                                                className={classNames(
                                                    item.current ? 'bg-gray-800 text-white' : 'text-gray-300 hover:bg-gray-700 hover:text-white',
                                                    'rounded-md px-3 py-2 text-sm font-medium'
                                                )}
                                                aria-current={item.current ? 'page' : undefined}
                                            >
                                                {item.name}
                                            </a>
                                        ))}
                                    </div>
                                </div>
                            </div>

                            {logado ? (
                                <div className="absolute inset-y-0 right-0 flex items-center pr-2 sm:static sm:inset-auto sm:ml-6 sm:pr-0">
                                    {/* <button
                                        type="button"
                                        className="relative rounded-full bg-gray-800 p-1 text-gray-400 hover:text-white focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800"
                                    >
                                        <span className="absolute -inset-1.5" />
                                        <span className="sr-only">View notifications</span>
                                        <span className="block h-6 w-6">🔔</span>
                                    </button> */}

                                    {/* Profile dropdown */}
                                    <Menu as="div" className="relative ml-3">
                                        <div>
                                            <MenuButton className="relative flex rounded-xl bg-gray-800 text-sm focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800 text-center items-center px-2">
                                                <span className="absolute -inset-1.5" />
                                                <span className="sr-only">Open user menu</span>
                                                <p className='p-3 text-white'>Seu Perfil</p>
                                                <Image
                                                    className="h-8 w-8 rounded-full"
                                                    src="/logo.png"
                                                    alt="User profile"
                                                    width={32}
                                                    height={32}
                                                />
                                            </MenuButton>
                                        </div>
                                        <Transition
                                            as={Fragment}
                                            enter="transition ease-out duration-100"
                                            enterFrom="transform opacity-0 scale-95"
                                            enterTo="transform opacity-100 scale-100"
                                            leave="transition ease-in duration-75"
                                            leaveFrom="transform opacity-100 scale-100"
                                            leaveTo="transform opacity-0 scale-95"
                                        >
                                            <MenuItems className="absolute right-0 z-10 mt-2 w-48 origin-top-right rounded bg-gray-900 py-1 shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none text-theme-text">
                                                <MenuItem>
                                                    {({ active }: { active: boolean }) => (
                                                        <Link
                                                            href="/usuario"
                                                            className={classNames(active ? 'bg-gray-800' : '', 'block px-4 py-2 text-sm')}
                                                        >
                                                            <p className='text-theme-text'>Seu Perfil</p>
                                                        </Link>
                                                    )}
                                                </MenuItem>
                                                {/* <MenuItem>
                                                    {({ active }: { active: boolean }) => (
                                                        <a
                                                            href="#"
                                                            className={classNames(active ? 'bg-gray-100' : '', 'block px-4 py-2 text-sm text-gray-700')}
                                                        >
                                                            Settings
                                                        </a>
                                                    )}
                                                </MenuItem> */}
                                                <MenuItem>
                                                    {({ active }: { active: boolean }) => (
                                                        <button
                                                            onClick={loggout}
                                                            type="button"
                                                            className={classNames(active ? 'bg-gray-800' : '', 'block px-4 py-2 text-sm')}
                                                        >
                                                            Sair
                                                        </button>
                                                    )}
                                                </MenuItem>
                                            </MenuItems>
                                        </Transition>
                                    </Menu>
                                </div>
                            ) : (
                                <div className='p-4 text-right'>
                                    <button onClick={entrar}>
                                        <div
                                            className="flex justify-center items-center font-bold w-[120px] h-[40px] text-center relative rounded-lg bg-gray-900 p-1 text-theme-button1 hover:text-white hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800"
                                        >
                                            Entrar
                                            {/* <span className="absolute -inset-1.5" />
                                            <span className="sr-only">Entrar</span>
                                            <span className="block h-6 w-6">Entrar</span> */}
                                        </div>
                                    </button>
                                </div>
                            )
                            }

                        </div>
                    </div>

                    <DisclosurePanel className="sm:hidden">
                        <div className="space-y-1 px-2 pb-3 pt-2">
                            {navigation.map((item) => (
                                <DisclosureButton
                                    key={item.name}
                                    as="a"
                                    href={item.href}
                                    className={classNames(
                                        item.current ? 'bg-gray-900 text-white' : 'text-gray-300 hover:bg-gray-700 hover:text-white',
                                        'block rounded-md px-3 py-2 text-base font-medium'
                                    )}
                                    aria-current={item.current ? 'page' : undefined}
                                >
                                    {item.name}
                                </DisclosureButton>
                            ))}
                        </div>
                    </DisclosurePanel>
                </>
            )}
        </Disclosure>
    )
}
