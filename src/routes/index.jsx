import { BrowserRouter, Routes, Route } from "react-router-dom";
import Main from "../pages/MainPage"; 
import Login from "../pages/LoginPage"; 
import Signup from "../pages/SignupPage";

const AppRouter = () => (
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<Main />} />
      <Route path="/login" element={<Login />} />
      <Route path="/signup" element={<Signup />} />
    </Routes>
  </BrowserRouter>
);

export default AppRouter;