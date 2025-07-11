import { BrowserRouter, Routes, Route } from "react-router-dom";
import Main from "../pages/MainPage"; 
import Login from "../pages/LoginPage"; 

const AppRouter = () => (
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<Main />} />
      <Route path="/login" element={<Login />} />
    </Routes>
  </BrowserRouter>
);

export default AppRouter;