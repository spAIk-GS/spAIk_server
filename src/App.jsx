import React, { useState, useEffect } from "react";
import AppContext from "./context/AppContext";
import AppRouter from "./routes/index";


function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

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