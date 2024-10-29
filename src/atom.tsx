import { atom } from "recoil";
import { recoilPersist } from "recoil-persist";

const { persistAtom: darkPersistAtom } = recoilPersist({
  key: "isDarkLocal",
  storage: localStorage,
});

export const isDarkAtom = atom({
  key: "isDark",
  default: true,
  effects_UNSTABLE: [darkPersistAtom],
});

const { persistAtom: clickPersistAtom } = recoilPersist({
  key: "isClickLocal",
  storage: localStorage,
});

export const isClickAtom = atom({
  key: "isClick",
  default: 1,
  effects_UNSTABLE: [clickPersistAtom],
});

const { persistAtom: totalPersistAtom } = recoilPersist({
  key: "isTotalLocal",
  storage: localStorage,
});

export const isTotalAtom = atom({
  key: "isTotal",
  default: 0,
  effects_UNSTABLE: [totalPersistAtom],
});

const { persistAtom: loggedInAtom } = recoilPersist({
  key: "isLoggedInLocal",
  storage: localStorage,
});

export const isLoggedInAtom = atom({
  key: "isLoggedIn",
  default: false,
  effects_UNSTABLE: [loggedInAtom],
});

export const memberIdsAtom = atom<number[]>({
  key: "memberIdsAtom",
  default: [],
});
