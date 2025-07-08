import React from 'react';
import './loginPage.scss';

function LoginPage() {
  return (
    <div className="login-container">
      <div className="login-card">
        <h2>Login For your Account</h2>

        <label htmlFor="email">Email</label>
        <input type="email" id="email" placeholder="Enter your email" />

        <label htmlFor="password">Password</label>
        <div className="password-wrapper">
          <input type="password" id="password" placeholder="Enter your password" />
          <a href="#" className="forgot-link">Forgot?</a>
        </div>

        <button className="login-btn">Login</button>

        <div className="or-divider">or</div>

        <button className="google-btn">
          <img src="https://upload.wikimedia.org/wikipedia/commons/5/53/Google_%22G%22_Logo.svg" alt="Google logo" />
          Sign In with Google
        </button>

        <p className="signup-text">
          No Account? <a href="#">Sign Up</a>
        </p>
      </div>

      <p className="terms-text">
        By using spAlk, you agree to our <a href="#">Terms</a> and <a href="#">Privacy Policy</a>
      </p>
    </div>
  );
}

export default LoginPage;
