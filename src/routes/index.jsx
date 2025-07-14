import { BrowserRouter, Routes, Route } from "react-router-dom";
import Main from "../pages/MainPage"; 
import Login from "../pages/LoginPage"; 
import Signup from "../pages/SignupPage";
import Profile from "../pages/ProfilePage";
import Upload from "../pages/UploadPage";

const AppRouter = () => (
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<Main />} />
      <Route path="/login" element={<Login />} />
      <Route path="/signup" element={<Signup />} />
      <Route path="/profile" element={<Profile />} />
      <Route path="/upload" element={<Upload />} />
    </Routes>
  </BrowserRouter>
);

export default AppRouter;