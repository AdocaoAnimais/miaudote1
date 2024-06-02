import Image from 'next/image';
import React from 'react';
import { FaDog, FaMars, FaVenus } from 'react-icons/fa';

interface CuteCardProps {
  imageUrl: string;
  title: string;
  size: string;
  gender: string;
  description: string;
}

const BlurCard = ({ imageUrl, title, size, description, gender }: CuteCardProps) => {
  const truncateDescription = (text: string, maxLength: number) => {
    if (text.length <= maxLength) return text;
    return text.slice(0, maxLength) + '...';
  };

  return (
    <div className="max-w-sm p-4 border rounded-lg shadow-lg bg-white/30 backdrop-blur-lg transition-transform transform hover:scale-105">
            <Image src={"/logo.png"} alt={"title"} width={400} height={400} className="rounded-lg" />
            <div className="mt-4">
                <h2 className="text-2xl font-bold">{"title"}</h2>
                <div className="flex items-center mt-2">
                    <FaDog className="mr-2 text-gray-500" />
                    <span className="text-gray-700">{"size"}</span>
                </div>
                <div className="flex items-center mt-2">
                    {gender === 'male' ? <FaMars className="mr-2 text-blue-500" /> : <FaVenus className="mr-2 text-pink-500" />}
                    <span className="text-gray-700">{"gender"}</span>
                </div>
                <p className="mt-4 text-gray-700">{"description"}</p>
                {truncateDescription(description, 100)}
            </div>
        </div>
  );
};

export default BlurCard;