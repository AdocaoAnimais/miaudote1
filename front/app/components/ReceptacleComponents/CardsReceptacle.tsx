import Image from "next/image";
import CuteCard from "./CardsReceptacle/CuteCard";
import CuteCard2 from "./CardsReceptacle/CuteCard2";
import CuteCard3 from "./CardsReceptacle/CuteCard3";
import BlurCard from "./CardsReceptacle/BlurCard";

export default function CardsReceptacle() {

    const gender = "male"
    const imageUrl = "/logo.png"
    const title = "Fofo Cachorro"
    const size = "Pequeno"
    const sex = "Fêmea"
    const description = "Este é um cachorrinho muito fofo e adorável que adora brincar e correr pelo parque. Ele é perfeito para uma família amorosa que tem tempo para dar a ele toda a atenção e carinho que ele merece."

    const truncate = (str: string, maxLength: number) => str.length > maxLength ? `${str.substring(0, maxLength)}...` : str;

    return (
        <>
            <div className="flex flex-wrap gap-20 justify-center items-center h-screen bg-gray-100">
                <CuteCard
                    imageUrl={imageUrl}
                    title={title}
                    size={size}
                    sex={sex}
                    description={description}
                />
                <CuteCard2
                    imageUrl={imageUrl}
                    title={title}
                    size={size}
                    sex={sex}
                    description={description}
                />
                <CuteCard3
                    imageUrl={imageUrl}
                    title={title}
                    size={size}
                    sex={sex}
                    description={description}
                />
                <BlurCard 
                    imageUrl={imageUrl}
                    title={title}
                    size={size}
                    gender={sex}
                    description={description}
                />
            </div>
        </>
    )
}