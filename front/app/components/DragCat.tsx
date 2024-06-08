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
        <div className="h-[30px] w-[30px]">
          <motion.div
            drag
            dragConstraints={constraintsRef}
            className="bg-blue-900"
          >
            <Image
              priority
              src={catIcon}
              alt="Cat"
              className="w-full h-full text-white"
            />
          </motion.div>
        </div>
      </motion.div>
    </>
  );
};
