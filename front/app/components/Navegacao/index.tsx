import Link from "next/link";

export function Navegacao() {
  return (
    <div className="bg-theme-background w-full text-color-text p-1 top-0 z-10 border-b border-black">
      <div className="text-center">
        <Link href="/">
          <h2 className="font-bold text-theme-secondary">Miaudote</h2>
        </Link>
      </div>
    </div>
  );
}
