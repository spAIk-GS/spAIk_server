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