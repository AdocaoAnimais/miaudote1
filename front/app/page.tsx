'use client'
import React, { use, useState } from 'react';
import Card from './components/Card.tsx';
import Link from 'next/link';
import { AuthenticationService } from '@/data/AuthenticationService';
import LinkButton_YellowTarja from './components/Buttons/LinkButton_YellowTarja.tsx';
import Image from 'next/image.js';
import CardsReceptacle from './components/ReceptacleComponents/CardsReceptacle.tsx';
import ButtonsReceptacle from './components/ReceptacleComponents/ButtonsReceptacle.tsx';
import Header from './components/Header.tsx';

export default function Page() {

    return (
        <>
            {/* 
                <CardsReceptacle />
                <ButtonsReceptacle /> 
            */}
            <div className='antialiased bg-theme-bg bg-center bg-cover bg-no-repeat min-h-[2000px] items-center flex flex-col'>
                <Header />
                <Card />
            </div>
        </>
    )
}
