'use-client'
import { AuthenticationService } from "@/data/AuthenticationService";
import { useRouter } from "next/navigation";

export default function Button_Sair() {
  const service = new AuthenticationService();
  const router = useRouter();
  function loggout() {
    service.logout(); 
    router.push('/cadastrar_animal');
  }
  return (
    <>
      <button
        type="reset"
        className="py-4 px-12 bg-[#0b132d] border border-[#f2a812] text-white rounded-md hover:bg-gray-300 focus:outline-none focus:bg-gray-300"
        onClick={loggout}
      >
        Sair
      </button>
    </>
  );
}
