import React from "react";
import "./SignupPage.scss";
import googleLogo from "../assets/google-logo.png"; // LoginPage에서 사용한 로고와 동일
import Header from "../components/Header";

function SignupPage() {
  return (
    <div className="signup-page-wrapper">
      <Header />
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





