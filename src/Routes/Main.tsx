import { useEffect, useState } from "react";
import {
  Box,
  BoxName,
  boxVar,
  Image,
  Name,
  Overlay,
  Slider,
  Title,
  WinBox,
  WinnerBox,
  WinnerImage,
  WinnerName,
  Wrapper,
} from "../Styles/Mainstyle";
import { useQuery, useQueryClient } from "react-query";
import {
  getRandommember,
  getSoccermember,
  getWorldcup,
  ISoccerMember,
  IWorldcup,
  postNextmember,
  postSelectmember,
} from "../api";
import { useNavigate, useParams } from "react-router-dom";
import { useRecoilState } from "recoil";
import { isClickAtom, isTotalAtom, memberIdsAtom } from "../atom";
import { AnimatePresence } from "framer-motion";
import styles from "../Styles/Main.module.css";

function Main() {
  const queryClient = useQueryClient();
  const { id } = useParams<{ id: string }>();
  const [clickquantity, setClickquantity] = useRecoilState(isClickAtom);
  const [totalquantity, setTotalquantity] = useRecoilState(isTotalAtom);
  const [clickId, setClickId] = useState<null | string>(null);
  const [isOverlayActive, setIsOverlayActive] = useState(false);
  const navigate = useNavigate();

  // 월드컵 이름 불러오기(worldcup table에서)
  const { data: worldcupData, isLoading: isLoadingWorldcup } = useQuery<
    IWorldcup[]
  >(["worldcup2", id], getWorldcup, {
    refetchOnWindowFocus: false,
  });

  // 월드컵 멤버 데이터 랜덤으로 불러오기(member table에서) => 게임을 하기 위함
  const { data: randomMembersData, isLoading: isLoadingRandomMembers } =
    useQuery<ISoccerMember[]>(
      ["randommember", id],
      () => getRandommember(Number(id)),
      {
        refetchOnWindowFocus: false,
      }
    );

  const [memberids, setMemberids] = useRecoilState(memberIdsAtom);
  useEffect(() => {
    if (randomMembersData && Array.isArray(randomMembersData)) {
      setMembers(randomMembersData);

      // member ID를 atom에 저장
      const ids = randomMembersData.map((member) => member.id);
      setMemberids(ids);
      ids.forEach((id) => {
        console.log(id); // 각 멤버의 id를 콘솔에 출력
      });
    }
  }, [randomMembersData, setMemberids]);

  // 월드컵 멤버 데이터 전체 들고 오기(member table에서) => 총 멤버가 몇명인지 알아야
  // 몇 게임을 진행해야 우승자가 나오는지 계산하기 위함
  const { data: worldcupmember, isLoading: isLoadingWorldcupmember } = useQuery<
    ISoccerMember[]
  >(["worldcupmember", id], () => getSoccermember(Number(id)), {
    refetchOnWindowFocus: false,
    onSuccess: (data) => {
      // 데이터가 성공적으로 로드되면 totalquantity를 업데이트
      if (data) {
        setTotalquantity(Math.floor(data.length / 2)); // length의 1/2로 설정
      }
    },
  });
  const [members, setMembers] = useState<ISoccerMember[]>(
    randomMembersData || []
  );

  const onSelect = async (memberId: number) => {
    if (isOverlayActive) return; // Overlay가 활성화된 경우 클릭 무시
    setClickId(memberId.toString());
    setIsOverlayActive(true);
    setTimeout(async () => {
      await postSelectmember(memberId);
      queryClient.invalidateQueries(["randommember", id]);
      setClickquantity(clickquantity + 1);
      setClickId(null);
      setIsOverlayActive(false);
    }, 300);
    console.log(memberId);
  };

  const onNextRound = async () => {
    if (clickquantity > totalquantity) {
      await postNextmember();
      queryClient.invalidateQueries(["randommember", id]);
      setClickquantity(1);
      setTotalquantity(totalquantity / 2);
    }
  };

  const gotoRank = () => {
    const worldcupid = worldcupData?.find(
      (worldcup) => worldcup.id === Number(id)
    )?.id;
    if (worldcupid) {
      navigate(`/main/${worldcupid}/rank`);
    }
  };

  return (
    <Wrapper>
      {isLoadingWorldcup || isLoadingRandomMembers ? (
        <div className={styles.loader}></div>
      ) : (
        worldcupData && (
          <>
            <Title>
              {
                worldcupData.find((worldcup) => worldcup.id === Number(id))
                  ?.name
              }
              월드컵
            </Title>

            {clickquantity <= totalquantity && (
              <Title>
                {clickquantity} / {totalquantity}
              </Title>
            )}

            <Slider>
              {clickquantity <= totalquantity &&
                members.map((member, index) => (
                  <Box
                    layoutId={member.id + ""}
                    key={member.id}
                    variants={boxVar(index)}
                    initial="invisible"
                    animate="visible"
                    exit="exit"
                    whileHover="hover"
                    bgPhoto={`http://localhost:8080${member.imageUrl}`}
                    onClick={() => onSelect(member.id)}
                    style={{ pointerEvents: isOverlayActive ? "none" : "auto" }} // Overlay 활성화 시 클릭 방지
                  >
                    <Name>{member.name}</Name>
                  </Box>
                ))}

              {members.length === 1 && (
                <WinnerBox>
                  <WinnerImage src="https://littledeep.com/wp-content/uploads/2020/03/littledeep_crown_style1.png" />
                  <Image src={`http://localhost:8080${members[0].imageUrl}`} />
                  <WinnerName>{members[0].name}</WinnerName>
                  <button className={styles.learn_more} onClick={gotoRank}>
                    <span className={styles.circle} aria-hidden="true">
                      <span className={styles.icon_arrow}></span>
                    </span>
                    <span className={styles.button_text}>랭킹보러가기</span>
                  </button>
                </WinnerBox>
              )}

              {clickquantity > totalquantity && members.length !== 1 && (
                <button className={styles.learn_more} onClick={onNextRound}>
                  <span className={styles.circle} aria-hidden="true">
                    <span className={styles.icon_arrow}></span>
                  </span>
                  <span className={styles.button_text}>Next Round</span>
                </button>
              )}

              <AnimatePresence>
                {clickId && (
                  <Overlay
                    onClick={() => setClickId(null)}
                    initial={{ backgroundColor: "rgba(0,0,0,0)" }}
                    animate={{ backgroundColor: "rgba(0,0,0,0.5)" }}
                    exit={{ backgroundColor: "rgba(0,0,0,0)" }}
                  >
                    {members.find((member) => member.id + "" === clickId) && (
                      <WinBox
                        layoutId={clickId}
                        bgPhoto={
                          `http://localhost:8080${
                            members.find((member) => member.id + "" === clickId)
                              ?.imageUrl
                          }` || "이미지가 없습니다!"
                        }
                      >
                        <BoxName>
                          {
                            members.find((member) => member.id + "" === clickId)
                              ?.name
                          }
                        </BoxName>
                      </WinBox>
                    )}
                  </Overlay>
                )}
              </AnimatePresence>
            </Slider>
          </>
        )
      )}
    </Wrapper>
  );
}

export default Main;
