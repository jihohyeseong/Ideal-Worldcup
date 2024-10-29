import styled from "styled-components";

export const Wrapper = styled.div`
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  /* flex-direction: column; */
`;

export const Slider = styled.div`
  display: flex;
  flex-wrap: wrap; // 여러 박스가 줄 바꿈이 가능하도록 설정
  /* justify-content: center; // 중앙 정렬 */
  margin-top: 50px;
`;

export const Title = styled.h1`
  font-size: 20px; // 크기 조정
  text-align: center;
  margin: 5px; // 위아래 여백 추가
`;

export const Description = styled.p`
  font-size: 16px; // 설명 텍스트 크기 조정
  text-align: center;
`;

export const Image = styled.img`
  width: 205px;
  height: 205px;
`;

export const Box = styled.div`
  height: 360px;
  width: 30%; // 각 Box의 너비를 30%로 설정하여 3개가 한 줄에 나오도록 함
  margin: 15px; // 여백을 유지
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 2px solid grey;
  background-color: ${(props) => props.theme.cardBgColor};
  box-shadow: 0 2px 3px rgba(0, 0, 0, 0.1), 0 10px 20px rgba(0, 0, 0, 0.06);
`;

export const RankImage = styled.img`
  width: 15px;
  height: 15px;
`;

export const GoodImage = styled.img`
  width: 18px;
  height: 15px;
`;

export const ImageWrapper = styled.div`
  display: flex; // 가로 방향으로 정렬
  align-items: center; // 가로 중앙 정렬
  margin-top: 5px;
`;

export const GoodImageWrapper = styled.div`
  display: flex; // 가로 방향으로 나열
  align-items: center; // 수직 중앙 정렬
`;

export const LikesCount = styled.span`
  margin-left: 2px; 
`;

export const ViewsCount = styled.span`
  margin-top: 5px; 
`;

export const EditButton = styled.button`
  color: ${(props) => props.theme.textColor};
`;

export const RankButton = styled.button`
  padding: 5px 10px;
  font-size: 15px;
  font-weight: 400;
  width: 100px;
  color: #f8ac59;
  border-radius: 5px;
  border: 2px solid #f8ac59;
  &:hover {
    background-color: #f8ac59;
    color: white;
  }
`;

export const GoodButton = styled.button`
  padding: 5px 10px;
  font-size: 15px;
  font-weight: 400;
  width: 100px;
  color: red;
  border-radius: 5px;
  border: 2px solid red;
  &:hover {
    background-color: red;
    color: white;
  }
`;
