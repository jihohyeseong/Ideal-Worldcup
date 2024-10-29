import { useQuery, useQueryClient } from "react-query";
import {
  deleteCommet,
  getComment,
  getSoccermember,
  IComment,
  ISoccerMember,
  patchComment,
  postComment,
} from "../api";
import { useNavigate, useParams } from "react-router-dom";
import { useState } from "react";
import {
  CommentBox,
  CommentContainer,
  CommentForm,
  Commenttext,
  CommentWrapper,
  Container,
  Img,
  Nametext,
  RankList,
  RanksList,
  WinNum,
  WinRateBox,
} from "../Styles/Rankstyle";
import { buildStyles, CircularProgressbar } from "react-circular-progressbar";
import "react-circular-progressbar/dist/styles.css";
import styles from "../Styles/Rank.module.css";
import { isDarkAtom } from "../atom";
import { useRecoilValue } from "recoil";

function Rank() {
  const queryClient = useQueryClient();
  const isDark = useRecoilValue(isDarkAtom);
  const textColor = isDark ? "#FFFFFF" : "#000000";
  const textColor2 = isDark ? "#000000" : "#ffffff";
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const { data, isLoading } = useQuery<ISoccerMember[]>(
    ["rankworldcup", id],
    () => getSoccermember(Number(id)),
    {
      refetchOnWindowFocus: false,
    }
  );

  const { data: commentdata, isLoading: isLoading2 } = useQuery<IComment[]>(
    ["comment", id],
    () => getComment(Number(id)),
    {
      refetchOnWindowFocus: false,
    }
  );
  const [commentContent, setCommentContent] = useState("");
  const newCommentContent = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (commentContent) {
      postComment(Number(id), {
        content: commentContent,
      })
        .then((newComment) => {
          setCommentContent("");
          // window.location.reload();
          queryClient.setQueryData<IComment[]>(["comment", id], (oldData) => {
            return oldData ? [...oldData, newComment] : [newComment];
          });
        })
        .catch((error) => {
          console.log("댓글 추가 실패: ", error);
          alert("댓글 추가에 실패하였습니다. 로그인이 필요한 서비스 입니다.");
          navigate("/login");
        });
    }
  };

  const [commentId, setCommentId] = useState<Number>(0);
  const [editCommentContent, setEditCommentContent] = useState("");
  const onEditCommentId = (comment: IComment) => {
    setCommentId(comment.id);
    setEditCommentContent(comment.content);
  };
  const onRevise = (
    n: Number,
    commentdata: { username: string; content: string; id: Number }
  ) => {
    patchComment(n, commentdata).then(() => {
      // 수정된 댓글을 commentdata에서 업데이트
      queryClient.setQueryData<IComment[]>(["comment", id], (oldData) => {
        return oldData
          ? oldData.map((comment) =>
              comment.id === n
                ? { ...comment, content: commentdata.content }
                : comment
            )
          : []; // oldData가 undefined일 경우 빈 배열 반환
      });
      setCommentId(0);
      setEditCommentContent("");
    });
  };
  const ondelete = (n: Number) => {
    deleteCommet(n).then(() => {
      // 삭제된 댓글을 commentdata에서 제거
      queryClient.setQueryData<IComment[]>(["comment", id], (oldData) => {
        return oldData ? oldData.filter((comment) => comment.id !== n) : []; // oldData가 undefined일 경우 빈 배열 반환
      });
    });
  };

  return (
    <Container>
      {isLoading && isLoading2 ? (
        <div className={styles.loader}></div>
      ) : (
        data && (
          <>
            <RanksList>
              {data.map((rank, index) => (
                <RankList key={rank.id || index}>
                  <a>
                    #{index + 1}
                    {rank.name}
                    <Img
                      src={`http://localhost:8080${rank.imageUrl}`}
                      alt="이미지 없음"
                    />
                    <WinRateBox>
                      <CircularProgressbar
                        value={Number(
                          (
                            (rank.winNum / (rank.winNum + rank.loseNum)) *
                            100
                          ).toFixed(2)
                        )}
                        text={
                          (
                            (rank.winNum / (rank.winNum + rank.loseNum)) *
                            100
                          ).toFixed(2) + "%"
                        }
                        circleRatio={0.75}
                        styles={buildStyles({
                          rotation: 1 / 2 + 1 / 8,
                          strokeLinecap: "butt",
                          trailColor: textColor,
                          textColor: textColor,
                          pathColor: textColor2,
                        })}
                      />
                    </WinRateBox>
                    <WinNum>{rank.victoryNum}회</WinNum>
                  </a>
                </RankList>
              ))}
            </RanksList>
            {commentdata && commentdata.length >= 0 && (
              <CommentWrapper>
                {commentdata.map((comment) => (
                  <CommentContainer key={comment.id}>
                    <Nametext>{comment.username}</Nametext>
                    {commentId === comment.id ? (
                      <>
                        <input
                          className={styles.input}
                          type="text"
                          value={editCommentContent}
                          onChange={(e) =>
                            setEditCommentContent(e.target.value)
                          }
                        />
                        <button
                          className={styles.Btn}
                          onClick={() =>
                            onRevise(comment.id, {
                              username: comment.username,
                              content: editCommentContent,
                              id: comment.id,
                            })
                          }
                        >
                          Edit
                          <svg className={styles.svg} viewBox="0 0 512 512">
                            <path d="M410.3 231l11.3-11.3-33.9-33.9-62.1-62.1L291.7 89.8l-11.3 11.3-22.6 22.6L58.6 322.9c-10.4 10.4-18 23.3-22.2 37.4L1 480.7c-2.5 8.4-.2 17.5 6.1 23.7s15.3 8.5 23.7 6.1l120.3-35.4c14.1-4.2 27-11.8 37.4-22.2L387.7 253.7 410.3 231zM160 399.4l-9.1 22.7c-4 3.1-8.5 5.4-13.3 6.9L59.4 452l23-78.1c1.4-4.9 3.8-9.4 6.9-13.3l22.7-9.1v32c0 8.8 7.2 16 16 16h32zM362.7 18.7L348.3 33.2 325.7 55.8 314.3 67.1l33.9 33.9 62.1 62.1 33.9 33.9 11.3-11.3 22.6-22.6 14.5-14.5c25-25 25-65.5 0-90.5L453.3 18.7c-25-25-65.5-25-90.5 0zm-47.4 168l-144 144c-6.2 6.2-16.4 6.2-22.6 0s-6.2-16.4 0-22.6l144-144c6.2-6.2 16.4-6.2 22.6 0s6.2 16.4 0 22.6z"></path>
                          </svg>
                        </button>
                      </>
                    ) : (
                      <div>
                        <Commenttext>{comment.content}</Commenttext>
                        {localStorage.getItem("username") ===
                          comment.username && (
                          <>
                            {" "}
                            <button
                              className={styles.edit_button}
                              onClick={() => onEditCommentId(comment)}
                            >
                              <svg
                                className={styles.edit_svgIcon}
                                viewBox="0 0 512 512"
                              >
                                <path d="M410.3 231l11.3-11.3-33.9-33.9-62.1-62.1L291.7 89.8l-11.3 11.3-22.6 22.6L58.6 322.9c-10.4 10.4-18 23.3-22.2 37.4L1 480.7c-2.5 8.4-.2 17.5 6.1 23.7s15.3 8.5 23.7 6.1l120.3-35.4c14.1-4.2 27-11.8 37.4-22.2L387.7 253.7 410.3 231zM160 399.4l-9.1 22.7c-4 3.1-8.5 5.4-13.3 6.9L59.4 452l23-78.1c1.4-4.9 3.8-9.4 6.9-13.3l22.7-9.1v32c0 8.8 7.2 16 16 16h32zM362.7 18.7L348.3 33.2 325.7 55.8 314.3 67.1l33.9 33.9 62.1 62.1 33.9 33.9 11.3-11.3 22.6-22.6 14.5-14.5c25-25 25-65.5 0-90.5L453.3 18.7c-25-25-65.5-25-90.5 0zm-47.4 168l-144 144c-6.2 6.2-16.4 6.2-22.6 0s-6.2-16.4 0-22.6l144-144c6.2-6.2 16.4-6.2 22.6 0s6.2 16.4 0 22.6z"></path>
                              </svg>
                            </button>
                            <button
                              className={styles.buttondelete}
                              onClick={() => ondelete(comment.id)}
                            >
                              <svg
                                viewBox="0 0 448 512"
                                className={styles.svgIcon}
                              >
                                <path d="M135.2 17.7L128 32H32C14.3 32 0 46.3 0 64S14.3 96 32 96H416c17.7 0 32-14.3 32-32s-14.3-32-32-32H320l-7.2-14.3C307.4 6.8 296.3 0 284.2 0H163.8c-12.1 0-23.2 6.8-28.6 17.7zM416 128H32L53.2 467c1.6 25.3 22.6 45 47.9 45H346.9c25.3 0 46.3-19.7 47.9-45L416 128z"></path>
                              </svg>
                            </button>
                          </>
                        )}
                      </div>
                    )}
                  </CommentContainer>
                ))}
                <CommentBox>
                  <CommentForm onSubmit={newCommentContent}>
                    <input
                      className={styles.input}
                      type="text"
                      placeholder="Please Write Your Comment"
                      value={commentContent}
                      onChange={(e) => setCommentContent(e.target.value)}
                    />
                    <button className={styles.buttonsend}>
                      <div className={styles.svg_wrapper_1}>
                        <div className={styles.svg_wrapper}>
                          <svg
                            xmlns="http://www.w3.org/2000/svg"
                            viewBox="0 0 24 24"
                            width="24"
                            height="24"
                          >
                            <path fill="none" d="M0 0h24v24H0z"></path>
                            <path
                              fill="currentColor"
                              d="M1.946 9.315c-.522-.174-.527-.455.01-.634l19.087-6.362c.529-.176.832.12.684.638l-5.454 19.086c-.15.529-.455.547-.679.045L12 14l6-8-8 6-8.054-2.685z"
                            ></path>
                          </svg>
                        </div>
                      </div>
                      <span>Send</span>
                    </button>
                  </CommentForm>
                </CommentBox>
              </CommentWrapper>
            )}
          </>
        )
      )}
    </Container>
  );
}

export default Rank;
