// src/components/StylishCard.tsx

import Image from 'next/image';
import React from 'react';

interface StylishCardProps {
  imageUrl: string;
  title: string;
  size: string;
  sex: string;
  description: string;
}

const CuteCard3 = ({ imageUrl, title, size, sex, description }: StylishCardProps) => {
  const truncateDescription = (text: string, maxLength: number) => {
    if (text.length <= maxLength) return text;
    return text.slice(0, maxLength) + '...';
  };

  return (
    <div className="max-w-sm rounded-lg overflow-hidden shadow-lg bg-gradient-to-r from-purple-700 to-indigo-900 p-6 transform transition-transform hover:scale-105">
      <Image src={"/logo.png"} alt={"title"} width={400} height={400} className="rounded-lg" />
      <div className="text-white">
        <div className="font-bold text-2xl mb-2">{title}</div>
        <div className="flex space-x-2 mb-4">
          <span className="bg-blue-600 text-white text-xs font-semibold px-3 py-1 rounded-full shadow-md">{size}</span>
          <span className="bg-pink-600 text-white text-xs font-semibold px-3 py-1 rounded-full shadow-md">{sex}</span>
        </div>
        <p className="text-gray-300 text-base mb-4">
          {truncateDescription(description, 100)}
        </p>
        <button className="bg-blue-500 text-white font-bold py-2 px-4 rounded-full transition-colors hover:bg-blue-600 shadow-lg">
          Saiba Mais
        </button>
      </div>
    </div>
  );
};

export default CuteCard3;
