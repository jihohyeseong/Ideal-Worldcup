import { motion } from "framer-motion";
import styled from "styled-components";

export const Nav = styled(motion.nav)`
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: fixed;
  width: 100%;
  top: 0;
  height: 60px;
  font-size: 14px;
  /* padding: 20px 60px; */
  padding-left: 50px;
  color: white;
  background-color: #00000088;
`;

export const Items = styled.ul`
  display: flex;
  align-items: center;
`;

export const Item = styled.li`
  margin-right: 15px;
  color: white;
  transition: color 0.3s ease-in-out;
  position: relative;
  display: flex;
  justify-content: center;
  flex-direction: column;
  &:hover {
    color: ${(props) => props.theme.accentColor};
  }
`;

export const Img = styled.img`
  width: 30px;
  height: 30px;
  margin-right: 10px;
`;
