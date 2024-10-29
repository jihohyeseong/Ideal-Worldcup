import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Login from "./Routes/Login";
import Header from "./Components/Header";
import Home from "./Routes/Home";
import Main from "./Routes/Main";
import Rank from "./Routes/Rank";
import Join from "./Routes/Join";
import CreateMain from "./Routes/CreateMain";
import CreateHome from "./Routes/CreateHome";

function App() {
  return (
    <Router>
      <Header />
      <Routes>
        <Route path="/main/:id/rank" element={<Rank />} />
        <Route path="/main/:id" element={<Main />} />
        <Route path="/login" element={<Login />} />
        <Route path="/create" element={<CreateHome />} />
        <Route path="/create/:id" element={<CreateMain />} />
        <Route path="/join" element={<Join />} />
        <Route path="/" element={<Home />} />
      </Routes>
    </Router>
  );
}

export default App;
