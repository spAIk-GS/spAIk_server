import React, { useState, useEffect } from "react";
import AppContext from "./context/AppContext";
import AppRouter from "./routes/index";
import AppContextProvider from "./context/AppContextProvider";



function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    const storedLogin = sessionStorage.getItem("isLoggedIn");
    if (storedLogin === "true") {
      setIsLoggedIn(true);
    }
  }, []);

  return (
    <AppContextProvider>
      <div className="App">
        <AppRouter />
      </div>
    </AppContextProvider>
  );
}

export default App;