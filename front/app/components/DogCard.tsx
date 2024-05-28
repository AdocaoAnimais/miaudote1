import React from 'react';

type DogCardProps = {
    imageUrl: string;
    title: string;
    size: 'Pequeno' | 'Médio' | 'Grande';
    gender: 'Macho' | 'Fêmea';
    description: string;
};

export default function DogCard({ imageUrl, title, size, gender, description }: DogCardProps) {
    
    const truncateText = (text: string, maxLength: number): string => {
        return text.length > maxLength ? text.slice(0, maxLength) + '...' : text;
    };

    return (
        <div className="max-w-sm rounded overflow-hidden shadow-lg bg-white p-4">

            <div className="px-6 py-4">
                <div className="font-bold text-xl mb-2">{title}</div>
                <div className="flex space-x-2 mb-4">
                    <span className="inline-block bg-blue-200 rounded-full px-3 py-1 text-sm font-semibold text-blue-700">{size}</span>
                    <span className="inline-block bg-pink-200 rounded-full px-3 py-1 text-sm font-semibold text-pink-700">{gender}</span>
                </div>
                <p className="text-gray-700 text-base">{truncateText(description, 100)}</p>
            </div>
        </div>
    );
};
