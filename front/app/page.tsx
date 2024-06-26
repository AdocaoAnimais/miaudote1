'use client'
import React from 'react';
import Card from './components/Card.tsx';   
import Header from './components/Header';

export default function Page() {

    return (
        <>
            {/* 
                <CardsReceptacle />
                <ButtonsReceptacle /> 
            */}
            <div className='antialiased bg-theme-bg bg-center bg-cover bg-no-repeat min-h-[2000px] items-center flex flex-col w-full'>
                <Header />
                <Card />
            </div>
        </>
    )
}
