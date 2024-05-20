export default function ButtonCancel({texto}:any) {
    (
        <>
            <button
                type="button"
                className="py-4 px-12 bg-[#0b132d] border border-[#f2a812] text-white rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300"
            >
                {texto}
            </button>
        </>
    )
}
