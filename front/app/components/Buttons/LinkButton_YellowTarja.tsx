interface LinkButtonProps {
    texto: string;
}

export default function LinkButton_YellowTarja({ texto }: LinkButtonProps) {
    return (
        <>
            <div
                className="w-[260px] h-[60px] flex justify-center items-center bg-theme-button1 text-white rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300"
            >
                {texto}
            </div>
        </>
    );
}
