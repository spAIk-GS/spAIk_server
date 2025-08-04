import { BrowserRouter, Routes, Route } from "react-router-dom";
import Main from "../pages/MainPage"; 
import Login from "../pages/LoginPage"; 
import Signup from "../pages/SignupPage";
import Profile from "../pages/ProfilePage";
import Upload from "../pages/UploadPage";
import TermsPage from "../pages/TermsPage";
import PolicyPage from "../pages/PolicyPage";
import ForgotPage from "../pages/ForgotPage";




const AppRouter = () => (
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<Main />} />
      <Route path="/login" element={<Login />} />
      <Route path="/signup" element={<Signup />} />
      <Route path="/profile" element={<Profile />} />
      <Route path="/upload" element={<Upload />} />
      <Route path="/terms" element={<TermsPage />} />
      <Route path="/policy" element={<PolicyPage />} />  
      <Route path="/forgot" element={<ForgotPage />} />

    </Routes>
  </BrowserRouter>
);

export default AppRouter;