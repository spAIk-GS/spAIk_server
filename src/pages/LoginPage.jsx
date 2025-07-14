import React from 'react';
import './LoginPage.scss';
import googleLogo from '../assets/google-logo.png';
import Header from "../components/Header";

function LoginPage() {
 return (

  <div className="login-page-wrapper login-page"> {/* ✅ 'login-page' 클래스 추가 */}

  <div className="login-page-wrapper">
      <Header />
    <div className="login-content">
      <div className="login-card">
        <h2>Login For your Account</h2>

        <label htmlFor="email">Email</label>
        <input type="email" id="email" placeholder="Enter your email" /> 


        <div className="password-label-row">
        <label htmlFor="password">Password</label>
        <a href="#" className="forgot-link">Forgot?</a>
        </div>
        <div className="password-wrapper">
        <input type="password" id="password" placeholder="Enter your password" />
        </div>


        <button className="login-btn">Login</button>
        

        <div className="or-divider">or</div>

        <button className="google-btn">
          <span>Sign In with Google</span>
          <img src={googleLogo} alt="Google logo" />  
        </button>

        <p className="signup-text">
          No Account? <a href="signup">Sign Up</a>
        </p>
      </div>

      <p className="terms-text">
        By using spAlk, you agree to our <a href="#">Terms</a> and <a href="#">Privacy Policy</a>
      </p>
    </div>
     </div>
     </div>
  );
}

export default LoginPage;
