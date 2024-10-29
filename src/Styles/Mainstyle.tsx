import { motion } from "framer-motion";
import styled from "styled-components";

export const Wrapper = styled.div`
  height: 95vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  margin-top: 20px;
`;

export const Title = styled.h1`
  font-size: 30px;
  text-align: center;
  margin-bottom: 20px;
`;

export const Slider = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
`;

export const Overlay = styled(motion.div)`
  width: 150%;
  height: 150%;
  position: absolute;
  display: flex;
  justify-content: center;
  align-items: center;
`;

export const Box = styled(motion.div)<{ bgPhoto: string }>`
  background-image: url(${(props) => props.bgPhoto});
  background-size: 100% 100%;
  height: 400px;
  width: 400px;
  margin: 10px;
  border: 2px solid grey;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 3px rgba(0, 0, 0, 0.1), 0 10px 20px rgba(0, 0, 0, 0.06);
`;

export const boxVar = (index: Number) => {
  return {
    hover: {
      scale: 1.1,
    },
    invisible: {
      x: index === 0 ? -500 : 500, // index가 0일 때 왼쪽, 1일 때 오른쪽
      opacity: 0,
      scale: 0,
    },
    visible: {
      x: 0,
      opacity: 1,
      scale: 1,
      transition: {
        duration: 0.55,
      },
    },
    exit: {
      x: index === 0 ? 500 : -500, // index가 0일 때 오른쪽, 1일 때 왼쪽
      opacity: 0,
      scale: 0,
    },
  };
};

export const WinBox = styled(motion.div)<{ bgPhoto: string }>`
  background-image: url(${(props) => props.bgPhoto});
  background-size: 100% 100%;
  height: 500px;
  width: 815px;
  margin: 10px;
  border: 2px solid grey;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 3px rgba(0, 0, 0, 0.1), 0 10px 20px rgba(0, 0, 0, 0.06);
`;

export const Image = styled.img`
  height: 400px;
  width: 400px;
`;

export const Name = styled.span`
  font-size: 28px;
  font-weight: 750;
  display: block;
  margin-bottom: 350px;
  margin-left: 220px;
  color: ${(props) => props.theme.boxTextColor};
`;

export const BoxName = styled.span`
  font-size: 28px;
  font-weight: 750;
  display: block;
  margin-bottom: 400px;
  margin-left: 620px;
  color: ${(props) => props.theme.boxTextColor};
`;

export const WinnerBox = styled.div`
  height: 500px;
  width: 500px;
  padding-top: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

export const WinnerImage = styled(motion.img)`
  height: 150px;
  width: 250px;
`;

export const WinnerName = styled.span`
  font-size: 28px;
  font-weight: 750;
  padding-top: 10px;
  padding-bottom: 10px;
  display: block;
  text-align: center;
  color: ${(props) => props.theme.textColor};
`;
