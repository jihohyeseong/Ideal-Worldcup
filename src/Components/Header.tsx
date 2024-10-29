import { Link } from "react-router-dom";
import { Img, Item, Items, Nav } from "../Styles/Headerstyle";
import { useRecoilState, useRecoilValue, useSetRecoilState } from "recoil";
import {
  isClickAtom,
  isDarkAtom,
  isLoggedInAtom,
  isTotalAtom,
  memberIdsAtom,
} from "../atom";
import { postClearLoseNum, postResetmember } from "../api";

function Header() {
  const isDark = useRecoilValue(isDarkAtom);
  const setDarkAtom = useSetRecoilState(isDarkAtom);
  const toggleDarkAtom = () => {
    setDarkAtom((current: boolean) => !current);
  };

  const isLoggedIn = useRecoilValue(isLoggedInAtom);
  const setIsLoggedIn = useSetRecoilState(isLoggedInAtom);
  const toggleIsLoggedIn = () => {
    setIsLoggedIn((current: boolean) => !current);
  };
  const handleLogout = () => {
    localStorage.removeItem("jwtToken");
    localStorage.removeItem("username");
    toggleIsLoggedIn();
    alert("로그아웃이 완료되었습니다!");
  };

  const [clickquantity, setClickquantity] = useRecoilState(isClickAtom);
  const [totalquantity, setTotalquantity] = useRecoilState(isTotalAtom);
  const memberids = useRecoilValue(memberIdsAtom);
  const onReset = () => {
    postResetmember();
    setClickquantity(1);
    setTotalquantity(16);
    const [id1, id2] = memberids;
    postClearLoseNum(id1, id2);
  };

  return (
    <Nav>
      <Items>
        <Item>
          <Link to="/" onClick={onReset}>
            Home
          </Link>
        </Item>
        <Item>
          {isLoggedIn ? (
            <Item>
              <Link to="/" onClick={handleLogout}>
                Logout
              </Link>
            </Item>
          ) : (
            <Item>
              <Link to="/login">LogIn</Link>
            </Item>
          )}
        </Item>
        <Item>
          {isLoggedIn ? (
            <Item>
              <Link to="/create">Create</Link>
            </Item>
          ) : null}
        </Item>
        <Item>
          {isDark ? (
            <Img
              src="https://cdn-icons-png.flaticon.com/512/6360/6360844.png"
              onClick={toggleDarkAtom}
            ></Img>
          ) : (
            <Img
              src="https://cdn-icons-png.flaticon.com/512/6714/6714978.png"
              onClick={toggleDarkAtom}
            ></Img>
          )}
        </Item>
      </Items>
    </Nav>
  );
}

export default Header;
