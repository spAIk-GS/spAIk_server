import React, { useState, useEffect } from "react";
import AppRouter from "./routes/index";
import AppContext from "./context/AppContext";


function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false); //로그인 상태
  

  // 새로고침 시 sessionStorage 확인
  useEffect(() => {
    const storedLogin = sessionStorage.getItem("isLoggedIn");
    if (storedLogin === "true") {
      setIsLoggedIn(true);
    }
  }, []);


  return (
    <AppContext.Provider value={{ isLoggedIn, setIsLoggedIn }}>
      <div className="App">
        <AppRouter />
      </div>
    </AppContext.Provider>
  );
}

export default App;
*/
import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import MainPage from "./pages/MainPage";
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;