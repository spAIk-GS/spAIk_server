import { useEffect, useState } from "react";
import AppContext from "./AppContext";

const AppContextProvider = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  // accessToken 기반으로 로그인 상태 초기화
  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    setIsLoggedIn(!!token); // 토큰 있으면 true, 없으면 false
    setIsLoading(false);
  }, []);

  return (
    <AppContext.Provider value={{ isLoggedIn, setIsLoggedIn, isLoading }}>
      {children}
    </AppContext.Provider>
  );
};

export default AppContextProvider;
