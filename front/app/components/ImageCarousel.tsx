'use client'

import Image from 'next/image';
import { useState } from 'react';

export function ImageCarousel({ images }: any) {
    const [currentImageIndex, setCurrentImageIndex] = useState(0);

    const nextImage = () => {
        setCurrentImageIndex((currentImageIndex + 1) % images.length);
    };

    const previousImage = () => {
        setCurrentImageIndex(
            (currentImageIndex - 1 + images.length) % images.length
        );
    };

    return (
        <div className='relative'>
            <div className={`relative h-[240px] w-full overflow-hidden rounded-xl`}>
                {images[currentImageIndex].url && (
                    <Image
                        key={currentImageIndex}
                        src={images[currentImageIndex].url}
                        alt={`Imagem ${currentImageIndex + 1}`}
                        fill
                        style={{ objectFit: 'cover' }}
                    />
                )}
                <div style={{ position: 'absolute', top: '50%', left: '5%', transform: 'translate(-50%, -50%)', zIndex: 1 }}>
                    <button onClick={previousImage}>{`<`}</button>
                </div>
                <div style={{ position: 'absolute', top: '50%', right: '4%', transform: 'translate(-50%, -50%)', zIndex: 1 }}>
                    <button onClick={nextImage}>{`>`}</button>
                </div>
            </div>
        </div>
    );
}