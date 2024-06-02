import Image from 'next/image';
import React from 'react';

interface CuteCardProps {
  imageUrl: string;
  title: string;
  size: string;
  sex: string;
  description: string;
}

const CuteCard = ({ imageUrl, title, size, sex, description }: CuteCardProps) => {
  const truncateDescription = (text: string, maxLength: number) => {
    if (text.length <= maxLength) return text;
    return text.slice(0, maxLength) + '...';
  };

  return (
    <div className="max-w-xs rounded-lg overflow-hidden shadow-lg bg-white p-4">
      <Image src={"/logo.png"} alt={"title"} width={400} height={400} className="rounded-lg" />
      <div className="px-4 py-2">
        <div className="font-bold text-xl mb-2">{title}</div>
        <div className="flex space-x-2 mb-2">
          <span className="bg-blue-200 text-blue-800 text-xs font-semibold mr-2 px-2.5 py-0.5 rounded-full">{size}</span>
          <span className="bg-pink-200 text-pink-800 text-xs font-semibold mr-2 px-2.5 py-0.5 rounded-full">{sex}</span>
        </div>
        <p className="text-gray-700 text-base">
          {truncateDescription(description, 100)}
        </p>
      </div>
    </div>
  );
};

export default CuteCard;