import { useQuery } from "react-query";
import { getWorldcup, IWorldcup, postGoodWorldcup } from "../api";
import { useNavigate } from "react-router-dom";
import {
  Box,
  Description,
  EditButton,
  GoodButton,
  GoodImage,
  GoodImageWrapper,
  Image,
  ImageWrapper,
  LikesCount,
  RankButton,
  RankImage,
  Slider,
  Title,
  ViewsCount,
  Wrapper,
} from "../Styles/Homestyle";
import { useRecoilValue } from "recoil";
import { isDarkAtom, isLoggedInAtom } from "../atom";
import styles from "../Styles/Main.module.css";

function Home() {
  const navigate = useNavigate();
  const isLoggedIn = useRecoilValue(isLoggedInAtom);
  const { data, isLoading } = useQuery<IWorldcup[]>(
    ["worldcup1"],
    getWorldcup,
    {
      refetchOnWindowFocus: false,
    }
  );
  const onClick = (id: number) => navigate(`/main/${id}`);
  const onClickMemeber = (id: number) => navigate(`/main/${id}/rank`);
  const onGood = (id: number) => {
    postGoodWorldcup(id);
    window.location.reload();
  };

  const onEdit = (id: number) => navigate(`/create/${id}`);

  // 데이터를 정렬하는 부분 추가
  const sortedData = data ? [...data].sort((a, b) => a.id - b.id) : [];

  return (
    <Wrapper>
      {isLoading ? (
        <div className={styles.loader}></div>
      ) : (
        <>
          <Slider>
            {sortedData.map((worldcup, index) => (
              <Box key={index}>
                <Image
                  src={`http://localhost:8080${worldcup.imageUrl}`}
                  onClick={() => onClick(worldcup.id)}
                />
                <Title>{worldcup.name}</Title>
                <Description>
                  {worldcup.description.length > 20
                    ? `${worldcup.description.slice(0, 20)}...`
                    : worldcup.description}
                </Description>

                <ImageWrapper>
                  <GoodImageWrapper>
                    <RankButton onClick={() => onClickMemeber(worldcup.id)}>
                      <RankImage src="https://cdn-icons-png.flaticon.com/512/2583/2583357.png" />
                      랭킹보기
                    </RankButton>
                  </GoodImageWrapper>
                  <GoodImageWrapper>
                    <GoodButton onClick={() => onGood(worldcup.id)}>
                      <GoodImage src="https://png.pngtree.com/png-vector/20220428/ourmid/pngtree-smooth-glossy-heart-vector-file-ai-and-png-png-image_4557871.png" />
                      <LikesCount>{worldcup.likesCount}</LikesCount>
                    </GoodButton>
                  </GoodImageWrapper>
                </ImageWrapper>
                <ViewsCount>조회수 {worldcup.viewsCount}</ViewsCount>
                {isLoggedIn ? (
                  <EditButton onClick={() => onEdit(worldcup.id)}>
                    편집
                  </EditButton>
                ) : null}
              </Box>
            ))}
          </Slider>
        </>
      )}
    </Wrapper>
  );
}

export default Home;
