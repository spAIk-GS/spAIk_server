import { createContext } from "react";

const AppContext = createContext({
    isLoggedIn: false,
    setIsLoggedIn: () => {},
    isLoading: true,
})

export default AppContext;