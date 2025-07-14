import React from "react";
import "./SignupPage.scss";
import logoImage from '../assets/logo.png';
import Header from "../components/Header";

function SignupPage() {
  return (
    <div className="signup-page-wrapper">
      <header className="header"
      style={{ backgroundColor: "#F9F9F9" }}
      >
        <a href="/" className="logo">
          <img src={logoImage} alt="logo" className="logo__img" />
          <span className="logo__text">spAIk</span>
        </a>
      </header>
      
       <div className="signup-content">
        <div className="signup-card">
          <h2>Let’s create your profile!</h2>

          <label htmlFor="name">Name</label>
          <input type="text" id="name" placeholder="Enter your name" />

          <label htmlFor="email">Email address</label>
          <input type="email" id="email" placeholder="Enter your email" />

          <label htmlFor="password">Password (8+ characters)</label>
          <input type="password" id="password" placeholder="Enter your password" />

          <label htmlFor="confirm-password">Re-type password</label>
          <input type="password" id="confirm-password" placeholder="Re-type your password" />

          <button className="signup-btn">Sign Up</button>
        </div>
      </div>
    </div>
  );
}

export default SignupPage;





