// src/main.jsx
import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
//import "./styles/main.scss"; // SCSS 전역 스타일이 있다면 여기에 import

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
