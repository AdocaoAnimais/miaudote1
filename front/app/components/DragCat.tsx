import * as React from "react";
import { useRef } from "react";
import { motion } from "framer-motion";
import Image from "next/image";
import catIcon from "../../public/cat.svg"

export const DragCat = () => {
  const constraintsRef = useRef(null);

  return (
    <>
      <motion.div ref={constraintsRef} className="bg-gray-900 w-full h-[80px]">
        <motion.div
          drag
          dragConstraints={constraintsRef}
          className="bg-blue-900 w-[30px] h-[30px]"
        >
          <Image
            priority
            src={catIcon}
            alt="Cat"
            className="w-[30px] h-[30px] text-white"
          />
        </motion.div>
      </motion.div>
    </>
  );
};
