import styled from "styled-components";

export const Container = styled.div`
  padding: 0px 20px;
  max-width: 960px; // 너비를 늘려서 두 요소가 나란히 배치될 수 있도록
  margin: 0 auto;
  margin-top: 90px;
  display: flex;
  justify-content: space-between; // 요소 간의 공간을 균등하게 배치
`;

export const Title = styled.h1`
  font-size: 48px;
  color: ${(props) => props.theme.textColor};
  text-align: center;
  margin-bottom: 20px;
`;

export const Img = styled.img`
  height: 75px;
  width: 75px;
  margin-left: 10px;
  margin-right: 10px;
`;

export const Loader = styled.div`
  height: 20vh;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
`;

export const RanksList = styled.ul`
  list-style: none;
  padding: 0;
  flex: 1; // RanksList가 가능한 공간을 차지하도록 설정
  margin-right: 20px; // 댓글창과의 간격을 위해 여백 추가
`;

export const RankList = styled.li`
  background-color: ${(props) => props.theme.cardBgColor};
  color: ${(props) => props.theme.textColor};
  border-radius: 20px;
  margin-bottom: 20px;
  border: 2px solid grey;
  display: flex;
  align-items: center;

  a {
    display: flex;
    align-items: center;
    padding: 20px;
    transition: color 0.2s ease-in;
    text-decoration: none;
    color: inherit;
  }

  &:hover {
    a {
      color: ${(props) => props.theme.accentColor};
    }
  }
`;

export const CommentWrapper = styled.div`
  flex: 1; // CommentWrapper가 가능한 공간을 차지하도록 설정
  margin-left: 20px; // RanksList와의 간격을 위해 여백 추가
  overflow-y: auto; // 댓글이 많을 경우 스크롤 가능하도록 설정
`;

export const CommentContainer = styled.div`
  margin: 8px 0;
  padding: 8px;
  display: flex;
  flex-direction: column;
  border: 2px solid grey;
  border-radius: 16px;
`;

export const Nametext = styled.span`
  color: ${(props) => props.theme.textColor};
  font-size: 20px;
  font-weight: 200;
  font-weight: bold;
  margin-bottom: 20px;
`;

export const Commenttext = styled.span`
  color: ${(props) => props.theme.textColor};
  font-size: 16px;
`;

export const WinRateBox = styled.div`
  width: 80px;
  height: 80px;
  margin-left: 10px;
  color: ${(props) => props.theme.textColor};
`;

export const WinNum = styled.span`
  margin-left: 20px;
  font-size: 20px;
  font-weight: 600;
  color: ${(props) => props.theme.textColor};
`;

export const CommentBox = styled.div`
  display: flex;
  flex-direction: column; /* 세로 방향으로 배열 */
  width: 100%; /* 전체 너비 사용 */
  margin-top: 20px; /* 댓글 목록과의 간격 추가 */
`;

export const CommentForm = styled.form`
  display: flex;
  width: 100%;
  margin-bottom: 10px;
`;
