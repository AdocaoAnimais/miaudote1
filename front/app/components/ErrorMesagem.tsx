import React from 'react';
import Image from 'next/image';

interface ErrorMessageProps {
    title: string;
    details: string;
    onClose: () => void;
}

const ErrorMessage: React.FC<ErrorMessageProps> = ({ title, details, onClose }) => {
    return (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
            <div className="bg-white p-6 rounded-lg shadow-lg text-center">
                <h2 className="text-xl font-bold mb-4">{title}</h2>
                <p className="mb-4">{details}</p>
                <button onClick={onClose} className="bg-blue-500 text-white px-4 py-2 rounded">
                    Fechar
                </button>
            </div>
        </div>
    );
};

export default ErrorMessage;
