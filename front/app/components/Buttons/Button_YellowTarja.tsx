export default function Button_YellowTarja({texto}:any) {
    return (
        <>
            <button
                type="submit"
                className="ml-2 py-4 px-20 bg-[#f2a812] text-white rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300"
            >
                {texto}
            </button>
        </>
    );
}
