export interface IWorldcup {
  id: number;
  name: string;
  imageUrl: string;
  description: string;
  createdAt: string;
  updatedAt: string;
  viewsCount: number;
  category: string;
  createdBy: string;
  likesCount: number;
}

export interface ISoccerMember {
  id: number;
  imageUrl: string;
  loseNum: number;
  name: string;
  victoryNum: number;
  winNum: number;
}

export interface IComment {
  content: string;
  id: number;
  memberId: number;
  username: string;
}

export function getWorldcup() {
  return fetch(`http://localhost:8080/worldcup`).then((response) =>
    response.json()
  );
}

export function getSoccermember(id: number) {
  return fetch(`http://localhost:8080/member/${id}/all`).then((response) =>
    response.json()
  );
}

export function getRandommember(id: number) {
  return fetch(`http://localhost:8080/member/${id}/random`).then((response) =>
    response.json()
  );
}

export function postResetmember() {
  return fetch(`http://localhost:8080/member/reset`, {
    method: "POST",
  }).then();
}

export function postSelectmember(id: number) {
  return fetch(`http://localhost:8080/member/select/${id}`, {
    method: "POST",
  }).then();
}

export function postNextmember() {
  return fetch(`http://localhost:8080/member/next`, {
    method: "POST",
  }).then();
}

export function postGoodWorldcup(id: number) {
  return fetch(`http://localhost:8080/worldcup/${id}/likes`, {
    method: "POST",
  }).then();
}

export function getComment(memberId: number) {
  return fetch(`http://localhost:8080/comment/${memberId}/all`).then(
    (response) => response.json()
  );
}

export function postComment(
  memberId: number,
  commentData: { content: string }
) {
  const token = localStorage.getItem("jwtToken");
  return fetch(`http://localhost:8080/comment/${memberId}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `${token ? token : ""}`,
    },
    body: JSON.stringify(commentData),
  }).then((response) => {
    if (!response.ok) {
      throw new Error("댓글 추가 실패");
    }
    return response.json();
  });
}

export function patchComment(
  id: Number,
  commentdata: {
    username: string;
    content: string;
    id: Number;
  }
) {
  const token = localStorage.getItem("jwtToken");
  return fetch(`http://localhost:8080/comment/${id}`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
      Authorization: `${token ? token : ""}`,
    },
    body: JSON.stringify(commentdata),
  }).then();
}

export function deleteCommet(id: Number) {
  const token = localStorage.getItem("jwtToken");
  return fetch(`http://localhost:8080/comment/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: `${token ? token : ""}`,
    },
  }).then();
}

export function postJoin(username: string, password: string) {
  return fetch(`http://localhost:8080/join`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ username, password }),
  });
}

export function postLogin(username: string, password: string) {
  return fetch(`http://localhost:8080/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ username, password }),
  }).then((response) => {
    if (response.ok) {
      localStorage.setItem("username", username);
      const token = response.headers.get("authorization");
      return token;
    }
  });
}

export function postClearLoseNum(id1: Number, id2: Number) {
  return fetch(`http://localhost:8080/member/clear/${id1}/${id2}`, {
    method: "POST",
  }).then();
}
